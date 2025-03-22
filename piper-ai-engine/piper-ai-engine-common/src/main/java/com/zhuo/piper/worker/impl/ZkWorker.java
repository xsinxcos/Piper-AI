package com.zhuo.piper.worker.impl;

import com.zhuo.piper.core.context.ITaskContext;
import com.zhuo.piper.task.Handler;
import com.zhuo.piper.worker.IWorker;
import org.springframework.stereotype.Component;

@Component
public class ZkWorker implements IWorker {

    @Override
    public void init() {

    }

    @Override
    public void run(ITaskContext<?> aTask ,Handler<?> handler) {

    }

}
