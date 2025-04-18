package com.zhuo.piper.core.task;


import com.zhuo.piper.core.context.task.execution.TaskExecution;

/**
 * 节点标识
 *
 * @param <O>
 */
public interface Handler<O> {
    O handle(TaskExecution aTask) throws Exception;
}
