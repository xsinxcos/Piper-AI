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

import java.util.ArrayList;
import java.util.List;
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
            // 入度为 0 的节点先进行 Lock 防止并发重复执行
            for (String nodeId : zeroInDegreeNodes) {
                dag.getNode(nodeId).lock();
            }
            // 获取入度为 0 的节点进行任务分配
            List<CompletableFuture<List<Object>>> futures = new ArrayList<>();
            // 异步
            DAG finalDag = dag;
            zeroInDegreeNodes.forEach(id -> {
                CompletableFuture<List<Object>> future = CompletableFuture.supplyAsync(() -> {
                    List<Object> re = new ArrayList<>();
                    SimpleTaskExecution taskExecution = new SimpleTaskExecution();
                    DAG copy = finalDag.deepCopy();
                    // 将需要的节点进行 unLock
                    copy.getNode(id).unLock();
                    handleNext(aTask ,copy);
                    re.add(copy);
                    re.add(taskExecution);
                    return re;
                }, executor);
                futures.add(future);
            });
            // 获取所有的结果
            List<DAG> dags = new ArrayList<>();
            List<TaskExecution> taskExecutions = new ArrayList<>();
            for (CompletableFuture<List<Object>> future : futures) {
                List<Object> objects = future.get();
                dags.add((DAG) objects.get(0));
                taskExecutions.add((TaskExecution) objects.get(1));
            }
            // 计算出最新的 Dag
            dag = DagUtils.getIntersection(dags);
        }
    }
}
