package com.zhuo.piper.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("local_message")
public class LocalMessagePO extends BasePO {
    private String id;

    private String messageId;

    private String messageBody;

    private String messageType;

    private Integer status; // 0:待发送, 1:已发送, 2:发送失败

    private Integer retryCount;

    private Integer maxRetryCount;

    private String errorMessage;
}
