package com.zhuo.piper.scheduler;

import com.zhuo.piper.context.task.execution.TaskExecution;

public interface Scheduler {

    void run(TaskExecution aTask , DAG dag);
}
