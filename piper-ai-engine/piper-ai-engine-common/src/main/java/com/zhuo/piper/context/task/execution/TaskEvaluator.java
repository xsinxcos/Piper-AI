package com.zhuo.piper.context.task.execution;

import com.zhuo.piper.context.Context;

public interface TaskEvaluator {
    /**
     * Evaluate the {@link TaskExecution}
     *
     * @param aJobTask
     *          The {@link TaskExecution} instance to evaluate
     * @param aContext
     *          The context to evaluate the task against
     * @return the evaluate {@link TaskExecution}.
     */
    TaskExecution evaluate (TaskExecution aJobTask, Context aContext);
}
