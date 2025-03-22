package com.zhuo.piper.drive.zk;

import com.zhuo.piper.drive.EventDriveConfiguration;
import com.zhuo.piper.drive.TopicMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;

public class ZookeeperEventDriver implements EventDriveConfiguration {

    @Resource
    private ZkServiceRegistration zkServiceRegistration;

    @Resource
    private CuratorFramework client;

    private ServiceDiscovery<Object> serviceDiscovery;

    @PostConstruct
    void init() throws Exception {
        zkServiceRegistration.register("piper-ai" ,"127.0.0.1" ,7070);
        serviceDiscovery = ServiceDiscoveryBuilder.builder(Object.class)
                .basePath("/piper-ai")
                .client(client)
                .build();
        serviceDiscovery.start();
    }

    @Override
    public void schedule(TopicMessage topicMessage) {

    }
}
