package com.zhuo.piper.core.context;

public class DSL {

    public static final String ID = "id";

    public static final String PARENT_ID = "parentId";

    public static final String JOB_ID = "jobId";

    public static final String DAG_NODE_ID = "dagNodeId";


    public static final String RETRY_ATTEMPTS = "retryAttempts";


    public static final String EXECUTION_TIME = "executionTime";

    public static final String CREATE_TIME = "createTime";


    public static final String START_TIME = "startTime";

    public static final String TASK_NUMBER = "taskNumber";


    public static final String END_TIME = "endTime";

    public static final String STATUS = "status";


    public static final String OUTPUT = "output";

    public static final String ERROR = "error";
    public static final String INPUT = "input";

    public static final String EXECUTION = "execution";

    public static final String PRIORITY = "priority";

    public static final String CURRENT_TASK = "currentTask";

    public static final String PARENT_TASK_EXECUTION_ID = "parentTaskExecutionId";

    public static final String input = "input";

    public static final String DAG = "dag";

    public static final String TRACE = "trace";

    public static final String TASK_EXECUTION = "taskExecution";

    public static final String[] RESERVED_WORDS = new String[]{
            ID,
            PARENT_ID,
            JOB_ID,
            RETRY_ATTEMPTS,
            EXECUTION_TIME,
            CREATE_TIME,
            START_TIME,
            TASK_NUMBER,
            END_TIME,
            STATUS,
            ERROR,
            EXECUTION,
            PRIORITY,
            CURRENT_TASK,
            PARENT_TASK_EXECUTION_ID,
            OUTPUT,
            INPUT
    };

}
