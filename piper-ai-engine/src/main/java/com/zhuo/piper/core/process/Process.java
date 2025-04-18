package com.zhuo.piper.core.process;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.model.aggregates.DAG;

/**
 * 流程节点标识
 */
public interface Process<O> {

    O run(TaskExecution aTask, DAG dag);

    ProcessType type();
}

