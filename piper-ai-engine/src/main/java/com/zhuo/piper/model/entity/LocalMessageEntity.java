package com.zhuo.piper.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalMessageEntity {
    private String id;

    private String messageId;

    private String messageBody;

    private String messageType;

    private Integer status; // 0:待发送, 1:已发送, 2:发送失败

    private Integer retryCount;

    private Integer maxRetryCount;

    private String errorMessage;
}
