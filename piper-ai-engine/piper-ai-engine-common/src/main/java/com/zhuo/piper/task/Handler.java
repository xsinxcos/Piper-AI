package com.zhuo.piper.task;


import com.zhuo.piper.context.task.execution.TaskExecution;

/**
 * 节点标识
 *
 * @param <O>
 */
public interface Handler<O> {
    O handle(TaskExecution aTask) throws Exception;
}
