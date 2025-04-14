package com.zhuo.piper.task.impl;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.task.DescHandler;
import com.zhuo.piper.task.TaskHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultHandler implements TaskHandler<String> , DescHandler {
    @Override
    public String handle(TaskExecution aTask) throws Exception {
        log.info("我执行了！！！！！！！！！！！！");
        return "我执行了！！！！！！！！！！！！";
    }

    @Override
    public String desc() {
        return "测试使用";
    }
}
