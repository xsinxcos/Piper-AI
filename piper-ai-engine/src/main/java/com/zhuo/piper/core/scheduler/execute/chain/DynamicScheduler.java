package com.zhuo.piper.core.scheduler.execute.chain;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.execute.IScheduler;
import com.zhuo.piper.core.scheduler.execute.AbstractSchedulerChain;
import com.zhuo.piper.model.aggregates.DAG;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DynamicScheduler extends AbstractSchedulerChain {

    private final Map<String, IScheduler> nextHandlers = new HashMap<>();

    public void addNext(String type, IScheduler scheduler) {
        nextHandlers.put(type, scheduler);
    }

    @Override
    public void run(TaskExecution aTask){
        DAG.DagNode node = (DAG.DagNode) aTask.getNode();
        if (node.getType() == 1) {
            nextHandlers.get("process").run(aTask);
        } else if (node.getType() == 0) {
            nextHandlers.get("task").run(aTask);
        }
        handleNext(aTask);
    }
}
