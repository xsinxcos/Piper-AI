package com.zhuo.piper.scheduler.chain;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.IScheduler;
import com.zhuo.piper.struct.DAG;
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
