package com.zhuo.piper.core.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class HandlerFactory {
    private final Map<String, Handler<?>> instances = new ConcurrentHashMap<>();

    @Autowired
    public HandlerFactory(List<Handler<?>> handlers) {
        handlers.forEach(handler -> {
            String className = handler.getClass().getName();
            instances.put(className, handler);
        });
    }

    public Handler<?> getInstance(String className) {
        return instances.get(className);
    }
}