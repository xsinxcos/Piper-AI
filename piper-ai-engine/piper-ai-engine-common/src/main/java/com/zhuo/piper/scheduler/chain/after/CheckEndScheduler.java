package com.zhuo.piper.scheduler.chain.after;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.struct.DAG;

import java.util.List;

public class CheckEndScheduler extends AbstractSchedulerChain {

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        // 将本次执行的节点移除
        dag.safeRemoveNode(aTask.getId());
        List<String> zeroInDegreeNodeIds = dag.getZeroInDegreeAndNoLockNodes();
        if(!zeroInDegreeNodeIds.isEmpty()){
            handleNext(aTask , dag);
        }
    }
}
