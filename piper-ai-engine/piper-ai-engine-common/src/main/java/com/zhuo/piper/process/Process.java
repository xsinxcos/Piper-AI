package com.zhuo.piper.process;

import com.zhuo.piper.Node;
import com.zhuo.piper.context.ITaskContext;

/**
 * 流程节点标识
 */
public interface Process<O> extends Node {

    O run(ITaskContext<?> aTask);

    ProcessType type();
}

