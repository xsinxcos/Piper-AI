package com.zhuo.piper.scheduler.chain.before;

import cn.zhuo.domain.common.util.SnowflakeIdGenerator;
import com.zhuo.piper.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.DAG;
import com.zhuo.piper.scheduler.Scheduler;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.task.TaskStatus;

import java.util.Date;

public class TaskExecutionInit  extends AbstractSchedulerChain {

    public TaskExecutionInit(Scheduler next) {
        this.setNext(next);
    }

    public TaskExecutionInit() {
    }

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        SimpleTaskExecution task = (SimpleTaskExecution) aTask;
        task.setStartTime(new Date());
        task.setId(SnowflakeIdGenerator.getInstance().nextIdStr());
        task.setStatus(TaskStatus.CREATING);
        handleNext(aTask ,dag);
    }
}
