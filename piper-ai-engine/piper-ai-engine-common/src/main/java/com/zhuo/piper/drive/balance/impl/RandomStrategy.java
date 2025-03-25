package com.zhuo.piper.drive.balance.impl;

import com.zhuo.piper.drive.balance.LoadBalanceStrategy;

import java.util.List;
import java.util.Random;

/**
 * 随机(Random)负载均衡策略
 * 随机选择一个可用节点
 */
public class RandomStrategy implements LoadBalanceStrategy {
    
    private final Random random = new Random();
    
    @Override
    public <T> T select(List<T> availableNodes, String key) {
        if (availableNodes == null || availableNodes.isEmpty()) {
            return null;
        }
        
        // 随机选择一个节点
        int index = random.nextInt(availableNodes.size());
        return availableNodes.get(index);
    }
    
    @Override
    public String getName() {
        return "Random";
    }
} 