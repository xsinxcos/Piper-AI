package com.zhuo.piper.core.scheduler.execute.chain;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.process.Process;
import com.zhuo.piper.core.process.ProcessFactory;
import com.zhuo.piper.core.scheduler.execute.AbstractSchedulerChain;
import com.zhuo.piper.model.aggregates.DAG;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ProcessScheduler extends AbstractSchedulerChain {

    @Resource
    private ProcessFactory processFactory;

    @Override
    public void run(TaskExecution aTask) {
        DAG.DagNode node = (DAG.DagNode) aTask.getNode();
        String className = node.getClassName();
        Process process = processFactory.getInstance(className);
        process.run(aTask);
        handleNext(aTask);
    }
}
