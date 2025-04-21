package com.zhuo.piper.core.scheduler.chain.before;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.IScheduler;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.model.aggregates.DAG;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DynamicSchedule extends AbstractSchedulerChain {

    private final Map<String, IScheduler> nextHandlers = new HashMap<>();

    public void addNext(String type, IScheduler scheduler) {
        nextHandlers.put(type, scheduler);
    }

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        String id = aTask.getDagNodeId();
        DAG.DagNode node = dag.getNode(id);
        if (node.getType() == 1) {
            nextHandlers.get("process").run(aTask, dag);
        } else if (node.getType() == 0) {
            nextHandlers.get("task").run(aTask, dag);
        }
        handleNext(aTask, dag);
    }
}
