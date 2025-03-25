package com.zhuo.piper.process;

import com.zhuo.piper.context.ITaskContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 流程节点职责链
 */
@Component
public class ProcessChain {
    private final List<Process<?>> processes;

    @Autowired
    public ProcessChain(List<Process<?>> processes){
        this.processes = processes;
    }

    public Object run(ITaskContext<?> aTask ,ProcessType type){
        for (Process<?> process : processes) {
            if(process.type().equals(type)){
                return process.run(aTask);
            }
        }
        return null;
    }
}
