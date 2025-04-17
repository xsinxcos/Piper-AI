package com.zhuo.piper.process;

import com.zhuo.piper.exception.EngineException;

public enum ProcessType {
    DEFAULT ,
    IF;

    public static ProcessType getType(String name){
        for (ProcessType value : values()) {
            if(value.name().equals(name)){
                return value;
            }
        }
        throw new EngineException("不存在该 Process : " + name);
    }
}
