package com.zhuo.piper.type.job;

import lombok.Getter;

@Getter
public enum JobStatus {
    CREATE(0),
    RUNNING(1),
    SUCCESS(2),
    FAIL(3);

    private final Integer status;

    JobStatus(Integer i) {
        status = i;
    }
}
