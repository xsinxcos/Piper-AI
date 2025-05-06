package com.zhuo.piper.core.context;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContextStore {
    private final ConcurrentHashMap<String ,Map<String , Object>> contextStore = new ConcurrentHashMap<>();

    public void merge(Object context ,String nodeId ,String jobId){
        if(context != null){
            Map<String, Object> map = contextStore.getOrDefault(jobId, new ConcurrentHashMap<>());
            map.put(nodeId ,context);
            contextStore.put(jobId ,map);
        }
    }

    public Object getByNodeId(String jobId ,String nodeId){
        Map<String, Object> map = contextStore.getOrDefault(jobId ,new ConcurrentHashMap<>());
        return map.get(nodeId);
    }

    public Map<String, Object> get(String jobId){
        return contextStore.getOrDefault(jobId ,new ConcurrentHashMap<>());
    }

    public void clear(String jobId){
        contextStore.remove(jobId);
    }
}
