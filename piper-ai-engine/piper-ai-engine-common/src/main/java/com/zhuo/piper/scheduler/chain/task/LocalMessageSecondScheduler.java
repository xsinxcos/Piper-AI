package com.zhuo.piper.scheduler.chain.task;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.save.LocalMessageService;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.struct.DAG;
import jakarta.annotation.Resource;

public class LocalMessageSecondScheduler extends AbstractSchedulerChain {
    @Resource
    private LocalMessageService localMessageService;

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        localMessageService.secondConfirm(aTask.getId());
    }
}
