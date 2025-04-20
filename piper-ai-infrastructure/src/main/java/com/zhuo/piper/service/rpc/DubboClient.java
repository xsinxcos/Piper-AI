package com.zhuo.piper.service.rpc;

import com.zhuo.piper.core.drive.RpcClient;
import com.zhuo.piper.core.drive.TopicMessage;
import com.zhuo.piper.type.http.Result;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * Dubbo client for service communication
 */
@Component
public class DubboClient {

    @DubboReference
    private RpcClient RpcClient;

    /**
     * Send a message to the Piper service
     * @param message The message to send
     * @return Result containing the response
     */
    public Result<String> sendMessage(TopicMessage message) {
        return RpcClient.processMessage(message);
    }
} 