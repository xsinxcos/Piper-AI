package com.zhuo.piper.scheduler;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.struct.DAG;

public interface IScheduler {

    void run(TaskExecution aTask , DAG dag);
}
