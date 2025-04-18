package com.zhuo.piper.core.scheduler.chain.task;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.service.ILocalMessageService;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class LocalMessageFirstScheduler extends AbstractSchedulerChain {
    @Resource
    private ILocalMessageService localMessageService;

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        // 写入消息表
        String msgBody = JsonUtils.toJson(Map.of("TaskExecution", aTask, "DAG", dag));
        String messageType = "task";
        localMessageService.firstConfirm(aTask.getId(), msgBody, messageType);
        try {
            handleNext(aTask, dag);
        } catch (Exception e) {
            log.error("执行失败：{}", e.getMessage());
            // 失败了，更新消息表
            localMessageService.failedConfirm(aTask.getId(), e.getMessage());
            throw e;
        }
    }
}
