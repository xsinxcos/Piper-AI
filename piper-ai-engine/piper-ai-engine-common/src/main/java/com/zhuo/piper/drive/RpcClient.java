package com.zhuo.piper.drive;

public interface RpcClient {

    void trigger(TopicMessage msg);

    void fin(TopicMessage param);

}
