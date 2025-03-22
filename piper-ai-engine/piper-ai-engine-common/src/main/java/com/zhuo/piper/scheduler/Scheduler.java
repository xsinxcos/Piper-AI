package com.zhuo.piper.scheduler;

import com.zhuo.piper.core.context.ITaskContext;

public interface Scheduler {

    void run(ITaskContext<?> aTask);
}
