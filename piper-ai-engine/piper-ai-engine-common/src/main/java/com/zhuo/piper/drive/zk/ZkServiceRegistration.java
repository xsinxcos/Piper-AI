package com.zhuo.piper.drive.zk;

import jakarta.annotation.Resource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class ZkServiceRegistration implements DisposableBean {
    @Resource
    private CuratorFramework client;

    private ServiceDiscovery<Object> serviceDiscovery;
    private ServiceInstance<Object> instance;

    public void register(String serviceName, String ip ,Integer port) throws Exception {
        // 构建服务实例信息
        instance = ServiceInstance.builder()
                .name(serviceName)  // 服务名称
                .address(ip)  // 实例IP
                .port(port)             // 实例端口
                .payload(new Object())  // 可选元数据
                .build();

        // 初始化ServiceDiscovery
        serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class)
                .basePath("/piper-ai")  // Zookeeper节点基础路径
                .client(client)
                .build();

        serviceDiscovery.start();
        serviceDiscovery.registerService(instance);
    }

    @Override
    public void destroy() throws Exception {
        if (serviceDiscovery != null) {
            serviceDiscovery.unregisterService(instance);
            serviceDiscovery.close();
        }
    }
}