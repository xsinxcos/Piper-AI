package com.zhuo.piper.scheduler.chain.before;

import cn.zhuo.domain.common.util.SnowflakeIdGenerator;
import com.zhuo.piper.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.task.TaskStatus;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TaskExecutionInit  extends AbstractSchedulerChain {

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        SimpleTaskExecution task = (SimpleTaskExecution) aTask;
        task.setStartTime(new Date());
        task.setId(SnowflakeIdGenerator.getInstance().nextIdStr());
        task.setStatus(TaskStatus.CREATING);
        handleNext(aTask ,dag);
    }
}
