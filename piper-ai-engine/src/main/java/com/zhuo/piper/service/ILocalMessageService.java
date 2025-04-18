package com.zhuo.piper.service;

public interface ILocalMessageService {

    void firstConfirm(String messageId, String messageBody, String messageType);

    void secondConfirm(String messageId);

    void failedConfirm(String messageId, String failReason);

    void tryConfirm(String messageId);


}
