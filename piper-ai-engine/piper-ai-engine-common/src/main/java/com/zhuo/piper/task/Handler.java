package com.zhuo.piper.task;


import com.zhuo.piper.context.ITaskContext;

/**
 * 节点标识
 * @param <O>
 */
public interface Handler<O> {
    O handle (ITaskContext<?> aTask) throws Exception;
}
