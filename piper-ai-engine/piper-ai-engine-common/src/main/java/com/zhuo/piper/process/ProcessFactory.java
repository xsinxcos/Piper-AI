package com.zhuo.piper.process;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProcessFactory {
    // Spring实例Map，Key为类名，Value为实例
    private final Map<String, Process<?>> instances = new ConcurrentHashMap<>();

    public ProcessFactory(List<Process> processList) {
        processList.forEach(process -> instances.put(process.getClass().getName(), process));
    }

    public Process getInstance(String className) {
        return instances.get(className);
    }
}
