package com.zhuo.piper.drive.zk;

import com.zhuo.piper.drive.TopicMessage;
import com.zhuo.piper.drive.balance.LoadBalancer;
import com.zhuo.piper.drive.zk.config.ZkConfig;
import com.zhuo.piper.exception.EngineException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ZkServiceFactory {
    @Resource
    private CuratorFramework client;
    @Resource
    private ZkConfig config;
    @Resource
    private LoadBalancer loadBalancer;
    private ServiceInstance<TopicMessage> instance;

    private ServiceDiscovery<TopicMessage> discovery;

    @PostConstruct
    public void init() throws Exception {
        // 初始化ServiceDiscovery
        discovery = ServiceDiscoveryBuilder.builder(TopicMessage.class)
                .basePath(config.getBaseUrl())  // Zookeeper节点基础路径
                .client(client)
                .build();
        discovery.start();
    }

    public ServiceInstance<TopicMessage> register() throws Exception {
        ServiceInstanceBuilder<TopicMessage> builder = ServiceInstance.builder();
        // 构建服务实例信息
        instance = builder
                .name(config.getServiceName())  // 服务名称
                .address(config.getIp())  // 实例IP
                .port(Integer.parseInt(config.getPort()))// 实例端口
                .build();
        discovery.registerService(instance);
        return instance;
    }


    // 获取所有实例
    public List<ServiceInstance<TopicMessage>> getInstances() {
        try {
            return discovery.queryForInstances(config.getServiceName()).stream().toList();
        } catch (Exception e) {
            throw new EngineException(" zk 获取所有实例失败：", e);
        }
    }

    public ServiceInstance<TopicMessage> getInstance() {
        List<ServiceInstance<TopicMessage>> instances = getInstances();
        return loadBalancer.select(instances, "piper-ai");
    }

    public void destroy(ServiceInstance<TopicMessage> instance) throws Exception {
        if (discovery != null) {
            discovery.unregisterService(instance);
            discovery.close();
        }
    }
}