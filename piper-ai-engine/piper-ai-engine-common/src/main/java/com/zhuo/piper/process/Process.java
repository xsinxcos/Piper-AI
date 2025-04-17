package com.zhuo.piper.process;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.struct.DAG;

/**
 * 流程节点标识
 */
public interface Process<O> {

    O run(TaskExecution aTask, DAG dag);

    ProcessType type();
}

