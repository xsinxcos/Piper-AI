package com.zhuo.piper.core.drive;

public interface RpcClient {

    Object trigger(TopicMessage msg);

}
