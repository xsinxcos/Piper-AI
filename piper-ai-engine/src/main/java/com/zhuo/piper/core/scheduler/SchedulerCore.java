package com.zhuo.piper.core.scheduler;

import com.zhuo.piper.model.entity.JobEntity;
import com.zhuo.piper.service.IJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SchedulerCore {

    private final DagBrain dagBrain;

    private final IJobService jobService;

    public void run(String dagId) {
        Optional<JobEntity> job = jobService.createJob(dagId);
        job.ifPresent(item -> {
            jobService.updateRunning(item.getId());
            dagBrain.enroll(dagId , item.getId());
        });
    }
}
