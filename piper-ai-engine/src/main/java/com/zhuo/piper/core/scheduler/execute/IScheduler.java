package com.zhuo.piper.core.scheduler.execute;

import com.zhuo.piper.core.context.task.execution.TaskExecution;

public interface IScheduler {

    void run(TaskExecution aTask);
}
