package com.zhuo.piper.core.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProcessFactory {
    private final Map<String, Process<?>> instances = new ConcurrentHashMap<>();

    @Autowired
    public ProcessFactory(List<Process<?>> processes) {
        processes.forEach(process -> {
            String className = process.getClass().getName();
            instances.put(className, process);
        });
    }

    public Process<?> getInstance(String className) {
        return instances.get(className);
    }
}
