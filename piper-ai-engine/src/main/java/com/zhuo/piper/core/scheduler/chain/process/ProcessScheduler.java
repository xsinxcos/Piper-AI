package com.zhuo.piper.core.scheduler.chain.process;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.process.Process;
import com.zhuo.piper.core.process.ProcessFactory;
import com.zhuo.piper.core.scheduler.DagBrain;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.model.aggregates.DAG;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ProcessScheduler extends AbstractSchedulerChain {

    @Resource
    private ProcessFactory processFactory;

    @Resource
    private DagBrain dagBrain;

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        String id = aTask.getDagNodeId();
        String className = dag.getNodes().get(id).getClassName();
        Process process = processFactory.getInstance(className);
        process.run(aTask, dag);
        handleNext(aTask, dag);
    }
}
