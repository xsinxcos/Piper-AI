package com.zhuo.piper.drive;

import lombok.Getter;

@Getter
public enum Topic {
    START("start"),
    RUNNING("running"),
    FINISH("finish"),
    ERROR("error");

    private final String topic;

    Topic(String topic) {
        this.topic = topic;
    }
}