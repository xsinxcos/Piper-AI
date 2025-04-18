package com.zhuo.piper.service.registry.balance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 负载均衡配置类
 * 支持通过配置文件指定负载均衡策略
 */
@Configuration
public class LoadBalanceConfig {

    @Value("${load.balance.strategy:RoundRobin}")
    private String loadBalanceStrategy;

    /**
     * 创建负载均衡器Bean
     *
     * @return 负载均衡器实例
     */
    @Bean
    public LoadBalancer loadBalancer() {
        LoadBalancer loadBalancer = new LoadBalancer();
        // 设置配置文件中指定的负载均衡策略
        loadBalancer.setStrategy(loadBalanceStrategy);
        return loadBalancer;
    }
} 