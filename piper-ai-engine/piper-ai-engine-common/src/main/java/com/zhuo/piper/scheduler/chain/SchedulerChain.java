package com.zhuo.piper.scheduler.chain;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.scheduler.IScheduler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerChain {
    private final List<IScheduler> schedulerChain;

    public SchedulerChain(List<IScheduler> schedulerChain) {
        this.schedulerChain = schedulerChain;
    }

    public void run(TaskExecution aTask, DAG dag){
        if(schedulerChain.isEmpty()){
            return;
        }
        schedulerChain.forEach(item -> item.run(aTask ,dag));
    }
}
