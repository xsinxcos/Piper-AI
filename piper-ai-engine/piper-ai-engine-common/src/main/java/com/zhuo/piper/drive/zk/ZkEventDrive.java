package com.zhuo.piper.drive.zk;

import com.zhuo.piper.drive.EventDrive;
import com.zhuo.piper.drive.RpcClient;
import com.zhuo.piper.drive.TopicMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ZkEventDrive implements EventDrive , DisposableBean {


    private ServiceInstance<TopicMessage> instance;

    @Resource
    private ZkServiceFactory zkServiceFactory;

    @Resource
    private RpcClient rpcClient;


    @PostConstruct
    void init() throws Exception {
        instance = zkServiceFactory.register();
    }

    @Override
    public void schedule(TopicMessage topicMessage) {
        ServiceInstance<TopicMessage> instance = zkServiceFactory.getInstance();
        rpcClient.trigger(new TopicMessage(instance.getAddress() + "/" + instance.getPort() ,
                UUID.randomUUID().toString() ,
                "test"));
    }

    @Override
    public void destroy() throws Exception {
        zkServiceFactory.destroy(instance);
    }
}
