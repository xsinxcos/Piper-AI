package com.zhuo.piper.service.registry.balance.impl;


import com.zhuo.piper.service.registry.ServiceInstance;
import com.zhuo.piper.service.registry.balance.LoadBalanceStrategy;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性哈希(Consistent Hash)负载均衡策略
 * 将相同key的请求分配到相同的节点，实现会话粘性
 */
public class ConsistentHashStrategy implements LoadBalanceStrategy {

    // 虚拟节点数量，增加虚拟节点可以让哈希分布更均匀
    private static final int VIRTUAL_NODE_COUNT = 160;

    @Override
    public <T> T select(List<T> availableNodes, String key) {
        if (availableNodes == null || availableNodes.isEmpty()) {
            return null;
        }

        // 如果没有提供key，退化为轮询策略
        if (key == null || key.isEmpty()) {
            return new RoundRobinStrategy().select(availableNodes, key);
        }

        // 创建哈希环
        SortedMap<Integer, T> hashRing = new TreeMap<>();

        // 将节点添加到哈希环
        for (T node : availableNodes) {
            // 获取节点ID
            String nodeId;
            if (node instanceof ServiceInstance) {
                nodeId = ((ServiceInstance) node).getNodeId();
            } else {
                // 如果不是WorkerNode，使用对象的hashCode作为nodeId
                nodeId = String.valueOf(node.hashCode());
            }

            // 为每个节点创建虚拟节点
            for (int i = 0; i < VIRTUAL_NODE_COUNT; i++) {
                String virtualNodeId = nodeId + "-" + i;
                int hash = getHash(virtualNodeId);
                hashRing.put(hash, node);
            }
        }

        // 如果哈希环为空，退化为轮询策略
        if (hashRing.isEmpty()) {
            return new RoundRobinStrategy().select(availableNodes, key);
        }

        // 计算key的哈希值
        int keyHash = getHash(key);

        // 找到第一个大于等于keyHash的节点
        SortedMap<Integer, T> subMap = hashRing.tailMap(keyHash);

        // 如果没有找到，则使用哈希环上的第一个节点
        if (subMap.isEmpty()) {
            return hashRing.get(hashRing.firstKey());
        }

        // 返回找到的节点
        return subMap.get(subMap.firstKey());
    }

    /**
     * 计算哈希值，使用FNV1_32_HASH算法
     *
     * @param key 键值
     * @return 哈希值
     */
    private int getHash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 防止负数出现
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    @Override
    public String getName() {
        return "ConsistentHash";
    }
} 