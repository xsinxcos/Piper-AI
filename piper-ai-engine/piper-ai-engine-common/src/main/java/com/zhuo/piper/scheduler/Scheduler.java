package com.zhuo.piper.scheduler;

import com.zhuo.piper.context.ITaskContext;

public interface Scheduler {

    void run(ITaskContext<?> aTask);
}
