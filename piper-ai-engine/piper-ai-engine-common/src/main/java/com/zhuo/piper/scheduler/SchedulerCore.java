package com.zhuo.piper.scheduler;


import com.zhuo.piper.context.ITaskContext;
import com.zhuo.piper.drive.EventDrive;
import com.zhuo.piper.scheduler.chain.SchedulerAfterChain;
import com.zhuo.piper.scheduler.chain.SchedulerBeforeChain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class SchedulerCore implements Scheduler{

    @Resource
    private SchedulerBeforeChain beforeChain;

    @Resource
    private SchedulerAfterChain afterChain;

    @Resource
    private EventDrive eventDrive;


    @Override
    public void run(ITaskContext<?> aTask) {
        beforeChain.run(aTask);


        afterChain.run(aTask);
    }
}
