package com.zhuo.piper.core.scheduler.execute.chain;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.execute.AbstractSchedulerChain;
import com.zhuo.piper.service.ILocalMessageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class LocalMessageSecondHandler extends AbstractSchedulerChain {
    @Resource
    private ILocalMessageService localMessageService;

    @Override
    public void run(TaskExecution aTask) {
        localMessageService.secondConfirm(aTask.getId());
    }
}
