package com.zhuo.piper.scheduler.chain;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.DAG;
import com.zhuo.piper.scheduler.Scheduler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerChain {
    private final List<Scheduler> schedulerChain;

    public SchedulerChain(List<Scheduler> schedulerChain) {
        this.schedulerChain = schedulerChain;
    }

    public void run(TaskExecution aTask, DAG dag){
        if(schedulerChain.isEmpty()){
            return;
        }
        schedulerChain.forEach(item -> item.run(aTask ,dag));
    }
}
