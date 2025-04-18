package com.zhuo.piper.service.registry;

public interface ServiceInstance {

    /**
     * 获取节点当前活跃连接数
     *
     * @return 活跃连接数
     */
    int getActiveConnections();

    /**
     * 获取节点ID
     *
     * @return 节点ID
     */
    String getNodeId();

    /**
     * 获取节点权重
     *
     * @return 节点权重，默认为1
     */
    default int getWeight() {
        return 1;
    }

    /**
     * 判断节点是否可用
     *
     * @return 如果节点可用返回true，否则返回false
     */
    default boolean isAvailable() {
        return true;
    }
}
