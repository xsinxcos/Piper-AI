package com.zhuo.piper.core.scheduler.chain.before;

import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.utils.DagUtils;
import jakarta.annotation.PostConstruct;
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
    @Qualifier("AsyncExecutor1")
    private Executor asyncExecutor1;

    @Autowired
    @Qualifier("AsyncExecutor2")
    private Executor asyncExecutor2;

    private Map<Integer, Executor> executorMap;

    @PostConstruct
    public void init() {
        executorMap = new HashMap<>();
        executorMap.put(1, asyncExecutor1);
        executorMap.put(2, asyncExecutor2);
    }


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
            int depth = aTask.getInteger("depth") == null ? 1 : aTask.getInteger("depth");
            // 获取入度为 0 的节点进行任务分配
            List<CompletableFuture<List<Object>>> futures = new ArrayList<>();
            // 异步
            DAG finalDag = dag;
            SimpleTaskExecution finalATask = (SimpleTaskExecution) aTask;
            // 根据不同的深度，安排不同的线程池，防止线程池死锁
            zeroInDegreeNodes.forEach(id -> {
                CompletableFuture<List<Object>> future = CompletableFuture.supplyAsync(() -> {
                    List<Object> re = new ArrayList<>();
                    DAG copy = finalDag.deepCopy();
                    SimpleTaskExecution copyTaskExecution = deepCopyTaskExecution(finalATask);
                    copyTaskExecution.set("depth", depth + 1);
                    // 将需要的节点进行 unLock
                    copy.getNode(id).unLock();
                    handleNext(copyTaskExecution, copy);
                    re.add(copy);
                    re.add(copyTaskExecution);
                    return re;
                }, executorMap.get(depth));
                futures.add(future);
            });
            // 获取所有的结果
            List<DAG> dags = new ArrayList<>();
            Map<String, Object> taskExecutionsMap = new HashMap<>();
            for (CompletableFuture<List<Object>> future : futures) {
//                List<Object> objects = future.get(10, TimeUnit.SECONDS);
                List<Object> objects = future.get();
                dags.add((DAG) objects.get(0));
                TaskExecution execution = (SimpleTaskExecution) objects.get(1);
                taskExecutionsMap.put(execution.getDagNodeId(), execution.getOutput());
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
