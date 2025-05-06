package com.zhuo.piper.core.scheduler.execute;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.exception.EngineException;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractSchedulerChain implements IScheduler {
    private IScheduler next;

    protected void handleNext(TaskExecution aTask) {
        try {
            if (next != null) {
                next.run(aTask);
            }
        } catch (Exception e) {
            throw new EngineException(e);
        }
    }
}
