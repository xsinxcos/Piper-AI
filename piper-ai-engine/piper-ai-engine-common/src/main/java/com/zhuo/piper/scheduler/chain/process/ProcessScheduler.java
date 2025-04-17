package com.zhuo.piper.scheduler.chain.process;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.process.Process;
import com.zhuo.piper.process.ProcessFactory;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.struct.DAG;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ProcessScheduler extends AbstractSchedulerChain {

    @Resource
    private ProcessFactory processFactory;

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        String id = aTask.getDagNodeId();
        String className = dag.getNodes().get(id).getClassName();
        Process process = processFactory.getInstance(className);
        process.run(aTask, dag);
        handleNext(aTask, dag);
    }
}
