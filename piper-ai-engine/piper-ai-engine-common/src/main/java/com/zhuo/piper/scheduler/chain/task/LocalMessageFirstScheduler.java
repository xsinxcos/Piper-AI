package com.zhuo.piper.scheduler.chain.task;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.save.LocalMessageService;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class LocalMessageFirstScheduler extends AbstractSchedulerChain {
    @Resource
    private LocalMessageService localMessageService;

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        // 写入消息表
        String msgBody = JsonUtils.toJson(Map.of("TaskExecution", aTask, "DAG", dag));
        String messageType = "task";
        localMessageService.firstConfirm(aTask.getId() ,msgBody ,messageType);
        try {
            handleNext(aTask, dag);
        }catch (Exception e){
            log.error("执行失败：{}" ,e.getMessage());
            // 失败了，更新消息表
            localMessageService.failedConfirm(aTask.getId(),e.getMessage());
            throw e;
        }
    }
}
