package com.zhuo.piper.parser;

import com.zhuo.piper.process.Process;
import com.zhuo.piper.task.TaskHandler;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DagNodeConfigParser implements Parser {
    @Resource
    private ApplicationContext applicationContext;

    public Process<?> getProcess(String className) {
        return (Process<?>) applicationContext.getBean(className);
    }

    public TaskHandler<?> getTaskHandler(String className) {
        return (TaskHandler<?>) applicationContext.getBean(className);
    }
}
