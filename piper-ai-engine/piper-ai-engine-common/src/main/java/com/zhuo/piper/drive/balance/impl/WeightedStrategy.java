package com.zhuo.piper.drive.balance.impl;

import com.zhuo.piper.drive.balance.LoadBalanceStrategy;
import com.zhuo.piper.worker.WorkerNode;

import java.util.List;
import java.util.Random;

/**
 * 加权(Weighted)负载均衡策略
 * 根据节点的权重分配负载，权重越高被选中的概率越大
 */
public class WeightedStrategy implements LoadBalanceStrategy {

    private final Random random = new Random();

    @Override
    public <T> T select(List<T> availableNodes, String key) {
        if (availableNodes == null || availableNodes.isEmpty()) {
            return null;
        }

        // 如果节点实现了WorkerNode接口，则使用权重选择
        if (!availableNodes.isEmpty() && availableNodes.get(0) instanceof WorkerNode) {
            int totalWeight = 0;
            for (T node : availableNodes) {
                WorkerNode workerNode = (WorkerNode) node;
                totalWeight += workerNode.getWeight();
            }

            // 如果总权重为0，则使用随机策略
            if (totalWeight <= 0) {
                return new RandomStrategy().select(availableNodes, key);
            }

            // 随机生成一个权重值
            int randomWeight = random.nextInt(totalWeight) + 1;

            // 根据权重选择节点
            int currentWeight = 0;
            for (T node : availableNodes) {
                WorkerNode workerNode = (WorkerNode) node;
                currentWeight += workerNode.getWeight();
                if (randomWeight <= currentWeight) {
                    return node;
                }
            }

            // 兜底逻辑，使用最后一个节点
            return availableNodes.get(availableNodes.size() - 1);
        }

        // 如果节点没有实现WorkerNode接口，则退化为随机策略
        return new RandomStrategy().select(availableNodes, key);
    }

    @Override
    public String getName() {
        return "Weighted";
    }
} 