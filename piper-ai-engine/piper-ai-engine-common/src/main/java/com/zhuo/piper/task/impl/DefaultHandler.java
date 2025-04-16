package com.zhuo.piper.task.impl;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.task.DescHandler;
import com.zhuo.piper.task.TaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class DefaultHandler implements TaskHandler<String> , DescHandler {
    @Override
    public String handle(TaskExecution aTask) throws Exception {
        log.info("我执行了！！！！！！！！！！！！");
        return new String("11".getBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public String desc() {
        return "测试使用";
    }
}
