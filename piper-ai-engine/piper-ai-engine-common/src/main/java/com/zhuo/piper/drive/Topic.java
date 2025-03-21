package com.zhuo.piper.drive;

import lombok.Getter;

@Getter
public enum Topic {
    PROCESS("Process"),
    TASK("Task");

    private final String topic;

    Topic(String topic) {
        this.topic = topic;
    }
}