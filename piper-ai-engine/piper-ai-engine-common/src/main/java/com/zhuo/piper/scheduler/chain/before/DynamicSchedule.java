package com.zhuo.piper.scheduler.chain.before;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.IScheduler;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.struct.DAG;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DynamicSchedule extends AbstractSchedulerChain {

    private final Map<String, IScheduler> nextHandlers = new HashMap<>();

    public void addNext(String type, IScheduler scheduler) {
        nextHandlers.put(type, scheduler);
    }

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        String id = aTask.getId();
        DAG.DagNode node = dag.getNode(id);
        if(node.getType() == 1){
            this.setNext(nextHandlers.get("1"));
        }else if(node.getType() == 0){
            this.setNext(nextHandlers.get("0"));
        }
        handleNext(aTask ,dag);
    }
}
