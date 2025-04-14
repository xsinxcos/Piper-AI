package com.zhuo.piper.process;

import com.zhuo.piper.context.Node;
import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.DAG;

/**
 * 流程节点标识
 */
public interface Process<O> extends Node {

    O run(TaskExecution aTask , DAG dag);

    ProcessType type();
}

