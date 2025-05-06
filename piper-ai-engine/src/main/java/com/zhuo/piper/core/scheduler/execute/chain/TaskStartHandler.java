package com.zhuo.piper.core.scheduler.execute.chain;

import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.execute.AbstractSchedulerChain;
import com.zhuo.piper.core.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskStartHandler extends AbstractSchedulerChain {

    @Override
    public void run(TaskExecution aTask) {
        SimpleTaskExecution task = (SimpleTaskExecution) aTask;
        task.setStatus(TaskStatus.EXECUTING);
        handleNext(task);
    }
}
