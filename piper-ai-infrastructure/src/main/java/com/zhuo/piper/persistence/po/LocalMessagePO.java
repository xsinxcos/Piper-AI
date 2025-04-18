package com.zhuo.piper.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("local_message")
public class LocalMessagePO {
    private String id;

    private String messageId;

    private String messageBody;

    private String messageType;

    private Integer status; // 0:待发送, 1:已发送, 2:发送失败

    private Integer retryCount;

    private Integer maxRetryCount;

    private String errorMessage;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
