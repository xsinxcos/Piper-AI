package com.zhuo.piper.service.registry.zookeeper;

import com.zhuo.piper.core.drive.zk.IZkServiceFactory;
import com.zhuo.piper.service.registry.balance.LoadBalancer;
import com.zhuo.piper.service.registry.zookeeper.exception.ZooKeeperException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;

import java.util.List;

//@Component
public class ZkServiceFactory implements IZkServiceFactory{
    @Resource
    private CuratorFramework client;
    @Resource
    private ZkConfig config;
    @Resource
    private LoadBalancer loadBalancer;

    private ServiceInstance<Object> instance;

    private ServiceDiscovery<Object> discovery;

    @PostConstruct
    public void init() throws Exception {
        // 初始化ServiceDiscovery
        discovery = ServiceDiscoveryBuilder.builder(Object.class)
                .basePath(config.getBaseUrl())  // Zookeeper节点基础路径
                .client(client)
                .build();
        discovery.start();
    }

    public ServiceInstance<Object> register() throws Exception {
        ServiceInstanceBuilder<Object> builder = ServiceInstance.builder();
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
    public List<ServiceInstance<Object>> getInstances() {
        try {
            return discovery.queryForInstances(config.getServiceName()).stream().toList();
        } catch (Exception e) {
            throw new ZooKeeperException(" zk 获取所有实例失败：", e);
        }
    }

    public ServiceInstance<Object> getInstance() {
        List<ServiceInstance<Object>> instances = getInstances();
        return loadBalancer.select(instances, "piper-ai");
    }

    public void destroy(ServiceInstance<Object> instance) throws Exception {
        if (discovery != null) {
            discovery.unregisterService(instance);
            discovery.close();
        }
    }
}