package cn.zhuo.engine.task;

import cn.zhuo.engine.core.context.ITaskContext;

/**
 * 节点标识
 * @param <O>
 */
public interface Handler<O> {
    O handle (ITaskContext<?> aTask) throws Exception;
}
