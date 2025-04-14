package com.zhuo.piper.worker.impl;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.task.Handler;
import com.zhuo.piper.worker.IWorker;
import org.springframework.stereotype.Component;

@Component
public class ZkWorker implements IWorker {

    @Override
    public void init() {

    }

    @Override
    public void run(TaskExecution aTask , Handler<?> handler) {
        try {
            handler.handle(aTask);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
