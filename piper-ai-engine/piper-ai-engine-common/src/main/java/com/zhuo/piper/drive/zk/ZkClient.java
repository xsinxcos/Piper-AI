package com.zhuo.piper.drive.zk;

import cn.zhuo.infrastructure.persistence.utils.HttpClient;
import com.zhuo.piper.drive.RpcClient;
import com.zhuo.piper.drive.TopicMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class ZkClient implements RpcClient , DisposableBean {
    private ServiceInstance<TopicMessage> instance;

    @Resource
    private ZkServiceFactory zkServiceFactory;

    @PostConstruct
    void init() throws Exception {
        instance = zkServiceFactory.register();
    }

    private static String ip = "http://%s:%s";

    @Override
    public Object trigger(TopicMessage param) {
        ServiceInstance<TopicMessage> send = zkServiceFactory.getInstance();
        String realIp = String.format(ip, send.getAddress(), send.getPort());
        HttpClient client = new HttpClient(realIp);
        return client.post("/" + param.getTopicName(), param, Object.class);
    }

    @Override
    public void destroy() throws Exception {
        zkServiceFactory.destroy(instance);
    }
}
