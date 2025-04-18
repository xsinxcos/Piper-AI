package com.zhuo.piper.type.http;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Result<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public Result() {
        this.code = AppHttpCodeEnum.SUCCESS.getCode();
        this.msg = AppHttpCodeEnum.SUCCESS.getMsg();
    }

    public Result(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> errorResult(int code, String msg) {
        Result<T> result = new Result<>();
        return result.error(code, msg);
    }

    public static <T> Result<T> okResult() {
        return new Result<>();
    }

    public static <T> Result<T> okResult(int code, String msg) {
        Result<T> result = new Result<>();
        return result.ok(code, null, msg);
    }

    public static <T> Result<T> okResult(T data) {
        Result<T> result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getMsg());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> Result<T> errorResult(AppHttpCodeEnum enums) {
        return setAppHttpCodeEnum(enums, enums.getMsg());
    }

    public static <T> Result<T> errorResult(AppHttpCodeEnum enums, String msg) {
        return setAppHttpCodeEnum(enums, msg);
    }

    public static <T> Result<T> setAppHttpCodeEnum(AppHttpCodeEnum enums) {
        return okResult(enums.getCode(), enums.getMsg());
    }

    private static <T> Result<T> setAppHttpCodeEnum(AppHttpCodeEnum enums, String msg) {
        return okResult(enums.getCode(), msg);
    }

    public Result<T> error(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public Result<T> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public Result<T> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        return this;
    }

    public Result<T> ok(T data) {
        this.data = data;
        return this;
    }

}