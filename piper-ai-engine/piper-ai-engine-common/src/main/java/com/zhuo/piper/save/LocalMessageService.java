package com.zhuo.piper.save;

import cn.zhuo.domain.common.util.SnowflakeIdGenerator;
import com.zhuo.piper.save.entity.LocalMessage;
import com.zhuo.piper.save.mapper.LocalMessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LocalMessageService {
    @Resource
    private LocalMessageMapper localMessageMapper;

    public void firstConfirm(String messageId ,String messageBody ,String messageType) {
        LocalMessage localMessage = LocalMessage.builder()
                .id(SnowflakeIdGenerator.getInstance().nextIdStr())
                .messageId(messageId)
                .retryCount(0)
                .maxRetryCount(3)
                .messageBody(messageBody)
                .messageType(messageType)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        localMessageMapper.insert(localMessage);
    }

    public void secondConfirm(String messageId) {
        localMessageMapper.updateStatusSuccess(messageId);
    }

    public void failedConfirm(String messageId ,String failReason) {
        localMessageMapper.updateStatusFailed(messageId ,failReason);
    }

    public void tryConfirm(String messageId) {
        LocalMessage localMessage = localMessageMapper.selectByMessageId(messageId);
        localMessage.setRetryCount(localMessage.getRetryCount()+1);
        localMessageMapper.updateById(localMessage);
    }
}
