package com.zhuo.piper.task;

public enum TaskStatus{
    /**
     * The task is in the process of being created.
     */
    CREATING,

    /**
     * The task is in the process of being executed.
     */
    EXECUTING,

    /**
     * The task has been successfully executed.
     */
    SUCCESS,

    /**
     * The task has failed to execute.
     */
    FAILED,

    /**
     * The task has been cancelled.
     */
    CANCELLED
}
