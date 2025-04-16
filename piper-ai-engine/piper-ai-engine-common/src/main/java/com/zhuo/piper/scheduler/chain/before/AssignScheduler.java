package com.zhuo.piper.scheduler.chain.before;

import com.zhuo.piper.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.utils.DagUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
public class AssignScheduler extends AbstractSchedulerChain {

    @Autowired
    @Qualifier(value = "AsyncExecutor")
    private Executor executor;

    @SneakyThrows
    @Override
    public void run(TaskExecution aTask, DAG dag) {
        while (!dag.getZeroInDegreeAndNoLockNodes().isEmpty()) {
            // 获取没有 Lock 且 入度为 0 的节点
            List<String> zeroInDegreeNodes = dag.getZeroInDegreeAndNoLockNodes();
            // 入度为 0 的节点先进行 Lock 防止重复执行
            for (String nodeId : zeroInDegreeNodes) {
                dag.getNode(nodeId).lock();
            }
            // 获取入度为 0 的节点进行任务分配
            List<CompletableFuture<List<Object>>> futures = new ArrayList<>();
            // 异步
            DAG finalDag = dag;
            SimpleTaskExecution finalATask = (SimpleTaskExecution)aTask;
            zeroInDegreeNodes.forEach(id -> {
                CompletableFuture<List<Object>> future = CompletableFuture.supplyAsync(() -> {
                    List<Object> re = new ArrayList<>();
                    DAG copy = finalDag.deepCopy();
                    SimpleTaskExecution copyTaskExecution = deepCopyTaskExecution(finalATask);
                    // 将需要的节点进行 unLock
                    copy.getNode(id).unLock();
                    handleNext(copyTaskExecution,copy);
                    re.add(copy);
                    re.add(copyTaskExecution);
                    return re;
                }, executor);
                futures.add(future);
            });
            // 获取所有的结果
            List<DAG> dags = new ArrayList<>();
            Map<String ,Object> taskExecutionsMap = new HashMap<>();
            for (CompletableFuture<List<Object>> future : futures) {
                List<Object> objects = future.get();
                dags.add((DAG) objects.get(0));
                TaskExecution execution = (SimpleTaskExecution) objects.get(1);
                taskExecutionsMap.put(execution.getDagNodeId() ,execution.getOutput());
            }
            // 计算出最新的 Dag
            dag = DagUtils.getIntersection(dags);
            // 计算出最新的 TaskExecution,但是 aTask 还是需要使用原来的
            SimpleTaskExecution simpleTaskExecution = (SimpleTaskExecution) aTask;
            simpleTaskExecution.setOutput(taskExecutionsMap);
        }
    }

    private SimpleTaskExecution deepCopyTaskExecution(SimpleTaskExecution taskExecution) {
        return SerializationUtils.clone(taskExecution);
    }
}
