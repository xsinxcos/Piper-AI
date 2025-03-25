package com.zhuo.piper.drive.zk;

import com.fasterxml.jackson.databind.JsonNode;
import com.zhuo.piper.drive.RpcServer;
import com.zhuo.piper.drive.TopicMessage;
import com.zhuo.piper.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ZkServer implements RpcServer {
    @Resource
    private ZkServiceFactory zkServiceFactory;

    @PostMapping("/trigger")
    public JsonNode trigger(@RequestBody TopicMessage msg){
        log.info(JsonUtils.toJson(msg));
        return null;
    }

    @GetMapping("/test")
    public String trigger0(){
        int size = zkServiceFactory.getInstances().size();
        return String.valueOf(size);
    }

}
