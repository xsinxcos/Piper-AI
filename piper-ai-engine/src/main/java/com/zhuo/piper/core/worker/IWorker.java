package com.zhuo.piper.core.worker;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.task.Handler;

public interface IWorker {
    void init();

    void run(TaskExecution aTask, Handler<?> handler);
}
