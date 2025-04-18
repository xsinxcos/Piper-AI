package com.zhuo.piper.core.drive.zk;

import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;

public interface IZkServiceFactory {

    ServiceInstance<Object> register() throws Exception;

    // 获取所有实例
    List<ServiceInstance<Object>> getInstances();

    ServiceInstance<Object> getInstance();

    void destroy(ServiceInstance<Object> instance) throws Exception;
}
