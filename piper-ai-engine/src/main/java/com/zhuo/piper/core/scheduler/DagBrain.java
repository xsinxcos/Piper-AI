package com.zhuo.piper.core.scheduler;

import com.zhuo.piper.core.scheduler.execute.TriggerContent;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.service.IDagService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@RequiredArgsConstructor
public class DagBrain {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private final ConcurrentHashMap<String, DAG> jobDagMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Set<String>> activity = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Lock> jobIdDagLock = new ConcurrentHashMap<>();

    private final IDagService dagService;

    @Resource(name = "AsyncExecutor")
    private Executor executor;

    /**
     * 将 DAG 注册到 DagBrain
     *
     * @param dagId
     * @return
     */
    public boolean enroll(String dagId, String jobId) {
        Optional<DAG> load = dagService.load(dagId);
        load.ifPresent(item -> {
            jobDagMap.put(jobId, item);
            jobIdDagLock.put(jobId, new ReentrantLock());
            checkAndAssign(jobId);
        });
        return load.isPresent();
    }

    /**
     * 将 子Dag图 展开拼接到 targetNode 之后
     *
     * @param subDagId
     * @param targetNodeId
     * @return
     */
    public boolean unfoldSubDag(String subDagId, String targetNodeId, String jobId) {
        Lock lock = jobIdDagLock.get(jobId);
        lock.lock();
        Optional<DAG> dag = dagService.loadSubDag(subDagId);
        dag.ifPresent(item -> {
            DAG parentDag = jobDagMap.get(jobId);
            parentDag.insertDAGAfterNode(targetNodeId, item);
        });
        lock.unlock();
        checkAndAssign(jobId);
        return true;
    }

    /**
     * 拉取一个待分配的节点（非线程安全）
     *
     * @param jobId
     * @return
     */
    private Optional<DAG.DagNode> select(String jobId) {
        List<DAG.DagNode> dagNodes = jobDagMap.get(jobId).getZeroInDegreeNodes();
        Set<String> activityList = activity.getOrDefault(jobId ,Collections.synchronizedSet(new HashSet<>()));
        for (DAG.DagNode node : dagNodes) {
            if(!activityList.contains(node.getId())){
                activityList.add(node.getId());
                activity.put(jobId ,activityList);
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    /**
     * 执行完后调用
     * @param jobId
     * @param nodeId
     */
    public void finish(String jobId ,String nodeId){
        Lock lock = jobIdDagLock.get(jobId);
        lock.lock();
        DAG dag = jobDagMap.get(jobId);
        dag.safeRemoveNode(nodeId);
        activity.get(jobId).remove(nodeId);
        checkAndAssign(jobId);
        allFinishCheckAndTrigger(jobId ,dag);
        lock.unlock();
    }

    /**
     * 当有写操作时就进行调用 检查并分配任务
     *
     * @param jobId
     */
    private void checkAndAssign(String jobId) {
        Lock lock = jobIdDagLock.get(jobId);
        lock.lock();
        Optional<DAG.DagNode> pull = select(jobId);
        while (pull.isPresent()) {
            DAG.DagNode dagNode = pull.get();
            asyncFirePropertyChange(ListenTopic.TRIGGER, null, new TriggerContent(jobDagMap.get(jobId).deepCopy() ,dagNode ,jobId));
            pull = select(jobId);
        }
        lock.unlock();
    }

    /**
     * 加载观察者
     * @param listener
     */
    public void loadTrigger(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }

    private void allFinishCheckAndTrigger(String jobId ,DAG dag){
        if(dag.getNodes().isEmpty()){
            releaseResource(jobId);
            asyncFirePropertyChange(ListenTopic.ALL_FINISH ,null ,jobId);
        }
    }

    private void asyncFirePropertyChange(String topic ,Object oldValue ,Object newValue){
        executor.execute(() -> support.firePropertyChange(topic ,oldValue ,newValue));
    }

    private void releaseResource(String jobId){
        jobDagMap.remove(jobId);
        jobIdDagLock.remove(jobId);
        activity.remove(jobId);
    }
}
