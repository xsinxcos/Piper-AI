package com.zhuo.piper.service.registry.zookeeper.exception;

public class ZooKeeperException extends RuntimeException {
    public ZooKeeperException(String message) {
        super(message);
    }
    public ZooKeeperException(String message, Throwable e) {
        super(message, e);
    }

}
