package com.zhuo.piper.task.impl;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.task.DescHandler;
import com.zhuo.piper.task.TaskHandler;
import com.zhuo.piper.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultHandler implements TaskHandler<String>, DescHandler {
    @Override
    public String handle(TaskExecution aTask) {
        log.info("我执行了！！！！！！！！！！！！");
        return JsonUtils.toJson("我执行了！！！！！！！！！！！！");
    }

    @Override
    public String desc() {
        return "测试使用";
    }
}
