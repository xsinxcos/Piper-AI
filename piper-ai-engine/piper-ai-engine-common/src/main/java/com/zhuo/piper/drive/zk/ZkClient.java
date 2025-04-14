package com.zhuo.piper.drive.zk;

import cn.zhuo.infrastructure.persistence.utils.HttpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.zhuo.piper.drive.RpcClient;
import com.zhuo.piper.drive.TopicMessage;
import com.zhuo.piper.utils.JsonUtils;
import org.springframework.stereotype.Component;

@Component
public class ZkClient implements RpcClient {

    @Override
    public void trigger(TopicMessage param) {
        String baseUrl = param.getTopicName();
        HttpClient client = new HttpClient(baseUrl);
        client.post("/trigger", JsonUtils.toJson(param), JsonNode.class);
    }

    @Override
    public void fin(TopicMessage param) {
        String baseUrl = param.getTopicName();
        HttpClient client = new HttpClient(baseUrl);
        client.post("/fin", JsonUtils.toJson(param), JsonNode.class);
    }
}
