package com.zhuo.piper.exception;

public class EngineException extends RuntimeException {
    public EngineException(String message) {
        super(message);
    }

    public EngineException(Throwable cause) {
        super(cause);
    }

    public EngineException(String message, Throwable e) {
        super(message, e);
    }
}
