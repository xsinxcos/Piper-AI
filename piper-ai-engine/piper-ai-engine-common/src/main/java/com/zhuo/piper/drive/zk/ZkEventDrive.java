package com.zhuo.piper.drive.zk;

import com.zhuo.piper.drive.EventDrive;
import com.zhuo.piper.drive.RpcClient;
import com.zhuo.piper.drive.TopicMessage;
import jakarta.annotation.Resource;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.stereotype.Component;

@Component
public class ZkEventDrive implements EventDrive {


    @Resource
    private ZkServiceFactory zkServiceFactory;

    @Resource
    private RpcClient rpcClient;


    @Override
    public Object schedule(TopicMessage topicMessage) {
        ServiceInstance<TopicMessage> instance = zkServiceFactory.getInstance();
        return rpcClient.trigger(topicMessage);
    }
}
