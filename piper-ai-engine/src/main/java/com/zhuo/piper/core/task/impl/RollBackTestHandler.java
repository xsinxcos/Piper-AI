package com.zhuo.piper.core.task.impl;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.task.DescHandler;
import com.zhuo.piper.core.task.RollBackHandler;
import com.zhuo.piper.core.task.TaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RollBackTestHandler implements RollBackHandler, TaskHandler<String>, DescHandler {
    @Override
    public String desc() {
        return "";
    }

    @Override
    public String handle(TaskExecution aTask) throws Exception {
        throw new Exception();
    }

    @Override
    public String executeRollBack(String taskId, String params) {
        log.info("我回滚了：{} {}", taskId, params);
        return "";
    }

    @Override
    public String getRollBackStatus(String taskId) {
        return "";
    }
}
