package com.zhuo.piper.worker;

import com.zhuo.piper.context.ITaskContext;
import com.zhuo.piper.task.Handler;

public interface IWorker {
    void init();

    void run(ITaskContext<?> aTask, Handler<?> handler);
}
