package com.zhuo.piper.service.registry.balance;

import java.util.List;

/**
 * 负载均衡策略接口
 * 策略模式的核心接口，定义所有负载均衡算法必须实现的方法
 */
public interface LoadBalanceStrategy {

    /**
     * 从可用节点列表中选择一个节点
     *
     * @param availableNodes 可用节点列表
     * @param key            可用于确定节点的键（例如，请求ID或任务ID）
     * @return 选中的节点，如果没有可用节点则返回null
     */
    <T> T select(List<T> availableNodes, String key);

    /**
     * 获取策略名称
     *
     * @return 策略名称
     */
    String getName();
} 