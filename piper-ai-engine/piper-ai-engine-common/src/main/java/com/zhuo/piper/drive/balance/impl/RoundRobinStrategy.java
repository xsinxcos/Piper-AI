package com.zhuo.piper.drive.balance.impl;

import com.zhuo.piper.drive.balance.LoadBalanceStrategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询(Round Robin)负载均衡策略
 * 按顺序循环选择可用节点
 */
public class RoundRobinStrategy implements LoadBalanceStrategy {
    
    private final AtomicInteger currentIndex = new AtomicInteger(0);
    
    @Override
    public <T> T select(List<T> availableNodes, String key) {
        if (availableNodes == null || availableNodes.isEmpty()) {
            return null;
        }
        
        // 使用AtomicInteger实现线程安全的轮询
        int index = currentIndex.getAndIncrement() % availableNodes.size();
        // 当计数器过大时重置，防止溢出
        if (currentIndex.get() > 10000) {
            currentIndex.set(index);
        }
        
        return availableNodes.get(index);
    }
    
    @Override
    public String getName() {
        return "RoundRobin";
    }
} 