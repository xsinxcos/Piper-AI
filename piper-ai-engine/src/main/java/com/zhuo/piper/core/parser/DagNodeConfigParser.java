package com.zhuo.piper.core.parser;

import com.zhuo.piper.core.process.Process;
import com.zhuo.piper.core.process.ProcessFactory;
import com.zhuo.piper.core.task.HandlerFactory;
import com.zhuo.piper.core.task.TaskHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class DagNodeConfigParser implements Parser {
    @Resource
    private ProcessFactory processFactory;

    @Resource
    private HandlerFactory handlerFactory;

    public Process<?> getProcess(String className) {
        return processFactory.getInstance(className);
    }

    public TaskHandler<?> getTaskHandler(String className) {
        return (TaskHandler<?>) handlerFactory.getInstance(className);
    }
}
