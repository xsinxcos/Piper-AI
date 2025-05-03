package com.zhuo.piper.core.scheduler.chain.before;

import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.model.entity.JobEntity;
import com.zhuo.piper.service.IJobService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JobStartHandler extends AbstractSchedulerChain {
    @Resource
    private IJobService jobService;

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        Optional<JobEntity> job = jobService.createJob(dag.getId());
        job.ifPresent(item -> {
            SimpleTaskExecution taskExecution = (SimpleTaskExecution) aTask;
            taskExecution.setJobId(item.getId());
            jobService.updateRunning(item.getId());
            handleNext(aTask ,dag);
        });
    }
}
