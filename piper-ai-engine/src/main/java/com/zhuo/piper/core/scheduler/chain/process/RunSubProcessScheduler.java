package com.zhuo.piper.core.scheduler.chain.process;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.model.aggregates.DAG;
import org.springframework.stereotype.Component;

@Component
public class RunSubProcessScheduler extends AbstractSchedulerChain {
    @Override
    public void run(TaskExecution aTask, DAG dag) {
        handleNext(aTask, dag);
    }
}
