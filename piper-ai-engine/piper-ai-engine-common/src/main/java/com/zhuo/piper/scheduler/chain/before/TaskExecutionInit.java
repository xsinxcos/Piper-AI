package com.zhuo.piper.scheduler.chain.before;

import cn.zhuo.domain.common.util.SnowflakeIdGenerator;
import com.zhuo.piper.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.task.TaskStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TaskExecutionInit  extends AbstractSchedulerChain {

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        // 获取没有 Lock 且 入度为 0 的节点，一般情况下为唯一
        List<String> zeroInDegreeNodes = dag.getZeroInDegreeAndNoLockNodes();
        String getNodeId = zeroInDegreeNodes.get(0);

        // 现在的 task 为 上一个节点的 task，需要初始化
        SimpleTaskExecution execution = new SimpleTaskExecution();
        execution.set(aTask.getDagNodeId() ,aTask.getOutput());
        execution.setStartTime(new Date());
        execution.setId(SnowflakeIdGenerator.getInstance().nextIdStr());
        execution.setDagNodeId(getNodeId);
        execution.setStatus(TaskStatus.CREATING);
        execution.setInput(dag.getNode(getNodeId).getConfig());
        handleNext(execution ,dag);
    }
}
