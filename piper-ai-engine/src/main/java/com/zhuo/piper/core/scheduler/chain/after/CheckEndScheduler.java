package com.zhuo.piper.core.scheduler.chain.after;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.model.aggregates.DAG;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckEndScheduler extends AbstractSchedulerChain {

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        // 将本次执行的节点移除
        dag.safeRemoveNode(aTask.getDagNodeId());
        List<String> zeroInDegreeNodeIds = dag.getZeroInDegreeAndNoLockNodes();
        if (!zeroInDegreeNodeIds.isEmpty()) {
            handleNext(aTask, dag);
        }
    }
}
