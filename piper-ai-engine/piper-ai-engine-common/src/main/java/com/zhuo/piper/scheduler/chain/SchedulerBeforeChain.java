package com.zhuo.piper.scheduler.chain;

import com.zhuo.piper.context.ITaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerBeforeChain {
    private final List<SchedulerBefore> schedulerBeforeChain;

    @Autowired
    public SchedulerBeforeChain(List<SchedulerBefore> schedulerBeforeChain) {
        this.schedulerBeforeChain = schedulerBeforeChain;
    }

    public void run(ITaskContext<?> aTask){
        schedulerBeforeChain.forEach(item -> item.run(aTask));
    }
}
