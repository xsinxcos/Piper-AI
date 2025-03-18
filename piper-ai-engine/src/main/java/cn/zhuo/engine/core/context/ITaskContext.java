package cn.zhuo.engine.core.context;


/**
 * 任务上下文接口
 */
public interface ITaskContext<O> {

    void put(String key, Object value);

    O get(String key);

}
