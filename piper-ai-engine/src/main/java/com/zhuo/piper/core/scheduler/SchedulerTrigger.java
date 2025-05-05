package com.zhuo.piper.core.scheduler;

import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.core.scheduler.chain.after.JobSuccessHandler;
import com.zhuo.piper.core.scheduler.chain.before.DynamicSchedule;
import com.zhuo.piper.core.scheduler.chain.before.JobStartHandler;
import com.zhuo.piper.core.scheduler.chain.before.TaskExecutionInitHandler;
import com.zhuo.piper.core.scheduler.chain.process.ProcessScheduler;
import com.zhuo.piper.core.scheduler.chain.process.RunSubProcessScheduler;
import com.zhuo.piper.core.scheduler.chain.task.TaskScheduler;
import com.zhuo.piper.model.aggregates.DAG;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@Component
public class SchedulerTrigger implements Trigger , PropertyChangeListener {
    private AbstractSchedulerChain schedulerChain;
    @Resource
    private DynamicSchedule dynamicSchedule;
    @Resource
    private ProcessScheduler processScheduler;
    @Resource
    private TaskScheduler taskScheduler;
    @Resource
    private TaskExecutionInitHandler taskExecutionInitHandler;
    @Resource
    private RunSubProcessScheduler runSubProcessScheduler;
    @Resource
    private JobStartHandler jobStartHandler;
    @Resource
    private JobSuccessHandler jobSuccessHandler;
    @Resource
    DagBrain dagBrain;
    @Resource
    ContextStore contextStore;

    @PostConstruct
    void init() {
        dagBrain.loadTrigger(this);
        // 在还没遍历完所有节点时，责任链成环

        taskExecutionInitHandler.setNext(dynamicSchedule);

        dynamicSchedule.addNext("task", taskScheduler);
        dynamicSchedule.addNext("process", processScheduler);

        processScheduler.setNext(runSubProcessScheduler);

        schedulerChain = taskExecutionInitHandler;
    }

    @Override
    public void run(TaskExecution execution ,DAG dag) {
        schedulerChain.run(execution ,dag);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("trigger".equals(evt.getPropertyName())){
            TriggerContent content = (TriggerContent) evt.getNewValue();
            SimpleTaskExecution simpleTaskExecution = new SimpleTaskExecution();
            simpleTaskExecution.setDagNodeId(content.getDagNode().getId());
            simpleTaskExecution.setJobId(content.getJobId());
            simpleTaskExecution.appendEnv(contextStore.get(content.getJobId()));
            schedulerChain.run(simpleTaskExecution ,content.getDag());
            contextStore.merge(simpleTaskExecution.getOutput() ,simpleTaskExecution.getDagNodeId() ,simpleTaskExecution.getJobId());
            dagBrain.finish(simpleTaskExecution.getJobId() ,simpleTaskExecution.getDagNodeId());
        }
    }
}
