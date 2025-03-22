package com.zhuo.piper.scheduler.chain;

import com.zhuo.piper.core.context.ITaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerAfterChain {

    private final List<SchedulerAfter> schedulerAfterChain;

    @Autowired
    public SchedulerAfterChain(List<SchedulerAfter> schedulerAfterChain) {
        this.schedulerAfterChain = schedulerAfterChain;
    }

    public void run(ITaskContext<?> aTask){
        schedulerAfterChain.forEach(item -> item.run(aTask));
    }
}
