package com.zhuo.piper.scheduler.chain.before;

import com.zhuo.piper.context.Node;
import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.DAG;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.scheduler.chain.process.ProcessScheduler;
import com.zhuo.piper.scheduler.chain.task.TaskScheduler;
import com.zhuo.piper.task.TaskHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ParallelSchedule extends AbstractSchedulerChain {

    private final Executor executor;

    private final ProcessScheduler processScheduler;

    private final TaskScheduler taskScheduler;

    public ParallelSchedule(Executor executor, ProcessScheduler processScheduler, TaskScheduler taskScheduler) {
        this.executor = executor;
        this.processScheduler = processScheduler;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        List<String> nodeIds = dag.getZeroInDegreeNodes();
        Map<String, Node> nodes = dag.getNodes();
        nodeIds.forEach(nodeId -> CompletableFuture.runAsync(() -> {
            Node node = nodes.get(nodeId);
            if(node instanceof Process){
                this.setNext(processScheduler);
                handleNext(aTask ,dag);
            }else if(node instanceof TaskHandler<?>){
                this.setNext(taskScheduler);
                handleNext(aTask ,dag);
            }
        } ,executor));
    }
}
