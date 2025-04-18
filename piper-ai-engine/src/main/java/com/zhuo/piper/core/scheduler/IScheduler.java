package com.zhuo.piper.core.scheduler;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.model.aggregates.DAG;

public interface IScheduler {

    void run(TaskExecution aTask, DAG dag);
}
