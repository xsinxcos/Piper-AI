package com.zhuo.piper.context.task.execution;

import com.zhuo.piper.context.Context;
import org.springframework.stereotype.Component;

@Component
public class DefaultTaskEvaluator implements TaskEvaluator {


    @Override
    public TaskExecution evaluate(TaskExecution aJobTask, Context aContext) {
        SimpleTaskExecution execution = new SimpleTaskExecution();
        execution.setInput(aContext);
        return execution;
    }
}
