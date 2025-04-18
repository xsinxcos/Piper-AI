package com.zhuo.piper.core.scheduler.chain;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.IScheduler;
import com.zhuo.piper.model.aggregates.DAG;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractSchedulerChain implements IScheduler {
    private IScheduler next;

    protected void handleNext(TaskExecution aTask, DAG dag) {
        if (next != null) {
            next.run(aTask, dag);
        }
    }
}
