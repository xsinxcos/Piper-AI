package com.zhuo.piper.drive.zk.config;

import lombok.Getter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ZkConfig {
    @Value("${zookeeper.connect-string}")
    private String connectString;
    @Value("${zookeeper.base-path}")
    private String baseUrl;
    @Value("${zookeeper.node.service-name}")
    private String serviceName;
    @Value("${zookeeper.node.ip}")
    private String ip;
    @Value("${zookeeper.node.port}")
    private String port;

    @Bean
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        client.start();
        return client;
    }
}