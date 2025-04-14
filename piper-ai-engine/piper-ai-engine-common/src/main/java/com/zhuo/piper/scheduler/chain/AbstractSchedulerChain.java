package com.zhuo.piper.scheduler.chain;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.DAG;
import com.zhuo.piper.scheduler.Scheduler;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractSchedulerChain implements Scheduler {
    private Scheduler next;

    protected void handleNext(TaskExecution aTask, DAG dag) {
        if (next != null) {
            next.run(aTask, dag);
        }
    }
}
