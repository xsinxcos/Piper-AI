package com.zhuo.piper.core.task.impl;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.task.DescHandler;
import com.zhuo.piper.core.task.TaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class DefaultHandler implements TaskHandler<Map>, DescHandler {
    @Override
    public Map handle(TaskExecution aTask) {
        log.info("我执行了！！！！！！！！！！！！");
        return Map.of("value" ,true);
    }

    @Override
    public String desc() {
        return "测试使用";
    }
}
