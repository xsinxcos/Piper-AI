package com.zhuo.piper.core.scheduler.execute.chain;

import com.zhuo.piper.core.context.ContextStore;
import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.DagBrain;
import com.zhuo.piper.core.scheduler.execute.AbstractSchedulerChain;
import com.zhuo.piper.core.task.TaskStatus;
import com.zhuo.piper.model.aggregates.DAG;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskFinishHandler extends AbstractSchedulerChain {
    @Resource
    DagBrain dagBrain;
    @Resource
    ContextStore contextStore;

    @Override
    public void run(TaskExecution aTask) {
        SimpleTaskExecution task = (SimpleTaskExecution) aTask;
        task.setStatus(TaskStatus.EXECUTING);
        DAG.DagNode node = (DAG.DagNode) task.getNode();
        contextStore.merge(aTask.getOutput() ,node.getId() ,aTask.getJobId());
        dagBrain.finish(aTask.getJobId() ,node.getId());
        handleNext(task);
    }
}
