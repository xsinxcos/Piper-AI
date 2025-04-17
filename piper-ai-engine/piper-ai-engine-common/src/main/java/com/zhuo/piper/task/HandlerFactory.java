package com.zhuo.piper.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class HandlerFactory {
    // Spring实例Map，Key为类名，Value为实例
    private static final Map<String, Handler<?>> instances = new ConcurrentHashMap<>();

    public HandlerFactory(List<Handler<?>> handlers) {
        for (Handler<?> handler : handlers) {
            instances.put(handler.getClass().getName(), handler);
        }
    }

    /**
     * 初始化Spring实例Map
     * 从Spring容器中获取所有实例，并存储到Map中
     */

    /**
     * 根据类名获取Spring实例
     *
     * @param className 完整的类名
     * @return Spring实例，如果不存在则返回null
     */
    public Handler<?> getInstance(String className) {
        return instances.get(className);
    }
}
