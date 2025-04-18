package com.zhuo.piper.core.scheduler.chain.task;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.service.ILocalMessageService;
import com.zhuo.piper.model.aggregates.DAG;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class LocalMessageSecondScheduler extends AbstractSchedulerChain {
    @Resource
    private ILocalMessageService localMessageService;

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        localMessageService.secondConfirm(aTask.getId());
    }
}
