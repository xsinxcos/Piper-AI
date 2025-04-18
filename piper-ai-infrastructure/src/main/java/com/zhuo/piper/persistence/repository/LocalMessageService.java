package com.zhuo.piper.persistence.repository;

import com.zhuo.piper.persistence.dao.LocalMessageMapper;
import com.zhuo.piper.persistence.po.LocalMessagePO;
import com.zhuo.piper.utils.SnowflakeIdGenerator;
import com.zhuo.piper.service.ILocalMessageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LocalMessageService implements ILocalMessageService {
    @Resource
    private LocalMessageMapper localMessageMapper;

    public void firstConfirm(String messageId, String messageBody, String messageType) {
        LocalMessagePO localMessagePO = LocalMessagePO.builder()
                .id(SnowflakeIdGenerator.getInstance().nextIdStr())
                .messageId(messageId)
                .retryCount(0)
                .maxRetryCount(3)
                .messageBody(messageBody)
                .messageType(messageType)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        localMessageMapper.insert(localMessagePO);
    }

    public void secondConfirm(String messageId) {
        localMessageMapper.updateStatusSuccess(messageId);
    }

    public void failedConfirm(String messageId, String failReason) {
        localMessageMapper.updateStatusFailed(messageId, failReason);
    }

    public void tryConfirm(String messageId) {
        LocalMessagePO localMessagePO = localMessageMapper.selectByMessageId(messageId);
        localMessagePO.setRetryCount(localMessagePO.getRetryCount() + 1);
        localMessageMapper.updateById(localMessagePO);
    }
}
