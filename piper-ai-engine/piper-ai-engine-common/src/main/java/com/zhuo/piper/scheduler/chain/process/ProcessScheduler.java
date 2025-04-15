package com.zhuo.piper.scheduler.chain.process;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.process.Process;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import org.springframework.stereotype.Component;

@Component
public class ProcessScheduler extends AbstractSchedulerChain {

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        String id = aTask.getId();
        Process<?> process = (Process<?>)dag.getNodes().get(id);
        process.run(aTask ,dag);
    }
}
