package com.zhuo.piper.core.drive.dubbo;

import com.zhuo.piper.core.drive.EventDrive;
import com.zhuo.piper.core.drive.RpcClient;
import com.zhuo.piper.core.drive.TopicMessage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Component
public class DubboEventDrive implements EventDrive {
    @DubboReference
    private RpcClient dubboClient;

    @Override
    public Object schedule(TopicMessage topicMessage) {
        return dubboClient.processMessage(topicMessage).getData();
    }
}
