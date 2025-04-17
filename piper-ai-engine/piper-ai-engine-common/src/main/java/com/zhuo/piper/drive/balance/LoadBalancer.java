package com.zhuo.piper.drive.balance;

import com.zhuo.piper.drive.balance.impl.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负载均衡器
 * 负责管理和使用不同的负载均衡策略
 */
@Slf4j
public class LoadBalancer {

    // 保存所有可用的负载均衡策略
    private final Map<String, LoadBalanceStrategy> strategies = new ConcurrentHashMap<>();

    /**
     * -- GETTER --
     * 获取当前使用的负载均衡策略
     *
     * @return 当前策略
     */
    // 当前使用的策略
    @Getter
    private LoadBalanceStrategy currentStrategy;

    /**
     * 初始化负载均衡器，注册内置策略
     */
    public LoadBalancer() {
        // 注册内置策略
        registerStrategy(new RoundRobinStrategy());
        registerStrategy(new RandomStrategy());
        registerStrategy(new LeastConnectionStrategy());
        registerStrategy(new WeightedStrategy());
        registerStrategy(new ConsistentHashStrategy());

        // 默认使用轮询策略
        currentStrategy = strategies.get("RoundRobin");
    }

    /**
     * 注册新的负载均衡策略
     *
     * @param strategy 负载均衡策略
     */
    public void registerStrategy(LoadBalanceStrategy strategy) {
        strategies.put(strategy.getName(), strategy);
        log.info("注册负载均衡策略: {}", strategy.getName());
    }

    /**
     * 设置当前使用的负载均衡策略
     *
     * @param strategyName 策略名称
     * @return 是否设置成功
     */
    public boolean setStrategy(String strategyName) {
        LoadBalanceStrategy strategy = strategies.get(strategyName);
        if (strategy != null) {
            currentStrategy = strategy;
            log.info("切换负载均衡策略为: {}", strategyName);
            return true;
        }
        log.warn("未找到负载均衡策略: {}", strategyName);
        return false;
    }

    /**
     * 获取所有已注册的策略名称
     *
     * @return 策略名称列表
     */
    public List<String> getAvailableStrategies() {
        return List.copyOf(strategies.keySet());
    }

    /**
     * 使用当前策略从可用节点中选择一个
     *
     * @param availableNodes 可用节点列表
     * @param key            可用于确定节点的键
     * @param <T>            节点类型
     * @return 选中的节点
     */
    public <T> T select(List<T> availableNodes, String key) {
        if (currentStrategy == null) {
            log.warn("未设置负载均衡策略，使用轮询策略");
            currentStrategy = strategies.get("RoundRobin");
        }

        T selected = currentStrategy.select(availableNodes, key);
        log.debug("负载均衡策略 {} 选择节点: {}", currentStrategy.getName(), selected);
        return selected;
    }

    /**
     * 使用指定策略从可用节点中选择一个
     *
     * @param strategyName   策略名称
     * @param availableNodes 可用节点列表
     * @param key            可用于确定节点的键
     * @param <T>            节点类型
     * @return 选中的节点，如果策略不存在则返回null
     */
    public <T> T select(String strategyName, List<T> availableNodes, String key) {
        LoadBalanceStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            log.warn("未找到负载均衡策略: {}", strategyName);
            return null;
        }

        T selected = strategy.select(availableNodes, key);
        log.debug("负载均衡策略 {} 选择节点: {}", strategy.getName(), selected);
        return selected;
    }
} 