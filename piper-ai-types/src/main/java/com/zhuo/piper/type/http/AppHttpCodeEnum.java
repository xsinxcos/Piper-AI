package com.zhuo.piper.type.http;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),

    SYSTEM_ERROR(500, "出现错误");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
