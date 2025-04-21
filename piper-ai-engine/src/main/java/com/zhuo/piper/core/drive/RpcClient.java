package com.zhuo.piper.core.drive;

import com.zhuo.piper.type.http.Result;

/**
 * Dubbo service interface for Piper service communication
 */
public interface RpcClient {
    /**
     * Process a topic message
     * @param message The topic message to process
     * @return Result containing the processing result
     */
    Result<Object> processMessage(TopicMessage message);
} 