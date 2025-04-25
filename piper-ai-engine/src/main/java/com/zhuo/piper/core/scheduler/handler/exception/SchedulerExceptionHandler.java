package com.zhuo.piper.core.scheduler.handler.exception;

import com.zhuo.piper.exception.EngineException;
import com.zhuo.piper.type.http.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SchedulerExceptionHandler {

    @ExceptionHandler(EngineException.class)
    public Result<String> handleEngineException(EngineException e) {
        //打印异常信息
        log.error("出现了异常:{}", String.valueOf(e));
        return Result.errorResult(500, e.getMessage());
    }
}
