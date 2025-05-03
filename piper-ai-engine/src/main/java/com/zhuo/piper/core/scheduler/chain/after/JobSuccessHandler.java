package com.zhuo.piper.core.scheduler.chain.after;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.service.IJobService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class JobSuccessHandler extends AbstractSchedulerChain {
    @Resource
    private IJobService jobService;

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        if(dag.getNodes().isEmpty()){
            jobService.updateSuccess(aTask.getJobId());
        }
        handleNext(aTask ,dag);
    }
}
