package com.zhuo.piper.core.drive.zk;

import com.zhuo.piper.core.drive.EventDrive;
import com.zhuo.piper.core.drive.RpcClient;
import com.zhuo.piper.core.drive.TopicMessage;
import org.apache.dubbo.config.annotation.DubboReference;

//@Component
public class ZkEventDrive implements EventDrive {

    @DubboReference
    private RpcClient dubboClient;

    @Override
    public Object schedule(TopicMessage topicMessage) {
        int retryTimes = 3;
        while (retryTimes > 0) {
            try {
                return dubboClient.processMessage(topicMessage).getData();
            } catch (Exception e) {
                retryTimes--;
                if (retryTimes == 0) {
                    throw new RuntimeException("Failed to process message after retries", e);
                }
            }
        }
        return null;
    }
}
