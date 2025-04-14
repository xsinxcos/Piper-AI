package com.zhuo.piper.worker;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.task.Handler;

public interface IWorker {
    void init();

    void run(TaskExecution aTask, Handler<?> handler);
}
