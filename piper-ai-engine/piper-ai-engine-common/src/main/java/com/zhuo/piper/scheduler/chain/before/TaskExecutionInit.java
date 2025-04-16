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
        SimpleTaskExecution task = (SimpleTaskExecution) aTask;
        // 现在的 task 为 上一个节点的 task，需要初始化
        if(aTask.getDagNodeId() != null){
            task.set(aTask.getDagNodeId() ,aTask.getOutput());
        }
        task.setStartTime(new Date());
        task.setId(SnowflakeIdGenerator.getInstance().nextIdStr());
        task.setDagNodeId(getNodeId);
        task.setInput(dag.getNode(getNodeId).getConfig());
        task.setStatus(TaskStatus.CREATING);

        handleNext(task ,dag);
    }
}
