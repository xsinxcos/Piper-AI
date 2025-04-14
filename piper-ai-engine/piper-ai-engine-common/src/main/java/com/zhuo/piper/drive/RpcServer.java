package com.zhuo.piper.drive;

import com.fasterxml.jackson.databind.JsonNode;

public interface RpcServer {

    JsonNode trigger(TopicMessage msg);
}
