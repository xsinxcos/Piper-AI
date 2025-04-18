package com.zhuo.piper.core.drive.zk;

import com.zhuo.piper.core.drive.RpcClient;
import com.zhuo.piper.core.drive.TopicMessage;
import com.zhuo.piper.utils.IHttpClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class ZkClient implements RpcClient, DisposableBean {
    private static String ip = "http://%s:%s";
    private ServiceInstance<Object> instance;
    @Resource
    private IZkServiceFactory zkServiceFactory;

    @Resource
    private IHttpClient httpClient;

    @PostConstruct
    void init() throws Exception {
        instance = zkServiceFactory.register();
    }

    @Override
    public Object trigger(TopicMessage param) {
        ServiceInstance<Object> send = zkServiceFactory.getInstance();
        String realIp = String.format(ip, send.getAddress(), send.getPort());
        return httpClient.post("/" + param.getTopicName(), param ,realIp).get("data");
    }

    @Override
    public void destroy() throws Exception {
        zkServiceFactory.destroy(instance);
    }
}
