package com.zhuo.piper.context.task.execution;

import com.zhuo.piper.context.Context;
import com.zhuo.piper.context.MapObject;

public class DefaultTaskEvaluator implements TaskEvaluator {
    @Override
    public TaskExecution evaluate(TaskExecution aJobTask, Context aContext) {
        SimpleTaskExecution execution = new SimpleTaskExecution(new MapObject());
        execution.setInput(aContext);
        return execution;
    }
}
