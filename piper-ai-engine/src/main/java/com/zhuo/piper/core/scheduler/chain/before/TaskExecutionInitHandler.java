package com.zhuo.piper.core.scheduler.chain.before;

import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.core.task.TaskStatus;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.utils.SnowflakeIdGenerator;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TaskExecutionInitHandler extends AbstractSchedulerChain {

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        String getNodeId = aTask.getDagNodeId();
        SimpleTaskExecution task = (SimpleTaskExecution) aTask;
        // 现在的 task 为 上一个节点的 task，需要初始化
        task.setStartTime(new Date());
        task.setId(SnowflakeIdGenerator.getInstance().nextIdStr());
        task.setInput(dag.getNode(getNodeId).getConfig());
        task.setStatus(TaskStatus.CREATING);

        handleNext(task, dag);
    }
}
