package com.zhuo.piper.core.drive.zk;

import com.zhuo.piper.core.drive.EventDrive;
import com.zhuo.piper.core.drive.RpcClient;
import com.zhuo.piper.core.drive.TopicMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ZkEventDrive implements EventDrive {

    @Resource
    private RpcClient rpcClient;


    @Override
    public Object schedule(TopicMessage topicMessage) {
        return rpcClient.trigger(topicMessage);
    }
}
