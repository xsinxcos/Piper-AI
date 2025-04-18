package com.zhuo.piper.service.registry.balance.impl;


import com.zhuo.piper.service.registry.ServiceInstance;
import com.zhuo.piper.service.registry.balance.LoadBalanceStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 最少连接(Least Connection)负载均衡策略
 * 选择当前活跃连接最少的节点
 */
public class LeastConnectionStrategy implements LoadBalanceStrategy {

    @Override
    public <T> T select(List<T> availableNodes, String key) {
        if (availableNodes == null || availableNodes.isEmpty()) {
            return null;
        }

        // 如果节点实现了WorkerNode接口，根据当前连接数排序选择
        if (!availableNodes.isEmpty() && availableNodes.get(0) instanceof ServiceInstance) {
            Optional<T> selected = availableNodes.stream()
                    .min((Comparator<T>) (o1, o2) -> {
                        ServiceInstance node1 = (ServiceInstance) o1;
                        ServiceInstance node2 = (ServiceInstance) o2;
                        return Integer.compare(node1.getActiveConnections(), node2.getActiveConnections());
                    });

            return selected.orElse(availableNodes.get(0));
        }

        // 如果节点没有实现WorkerNode接口，则退化为轮询策略
        return new RoundRobinStrategy().select(availableNodes, key);
    }

    @Override
    public String getName() {
        return "LeastConnection";
    }
} 