package com.zhuo.piper.core.scheduler.execute;

import com.zhuo.piper.core.context.ContextStore;
import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.DagBrain;
import com.zhuo.piper.core.scheduler.ListenTopic;
import com.zhuo.piper.core.scheduler.execute.chain.*;
import com.zhuo.piper.core.task.TaskStatus;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.utils.SnowflakeIdGenerator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@Component
public class SchedulerTrigger implements  PropertyChangeListener {
    private AbstractSchedulerChain schedulerChain;
    @Resource
    private DynamicScheduler dynamicScheduler;
    @Resource
    private ProcessScheduler processScheduler;
    @Resource
    private TaskScheduler taskScheduler;
    @Resource
    private TaskStartHandler taskStartHandler;
    @Resource
    private TaskFinishHandler taskFinishHandler;
    @Resource
    DagBrain dagBrain;
    @Resource
    ContextStore contextStore;

    @PostConstruct
    void init() {
        dagBrain.loadTrigger(this);

        taskStartHandler.setNext(dynamicScheduler);

        dynamicScheduler.addNext("task", taskScheduler);
        dynamicScheduler.addNext("process", processScheduler);

        taskScheduler.setNext(taskFinishHandler);
        processScheduler.setNext(taskFinishHandler);

        schedulerChain = taskStartHandler;
    }

    /**
     * 触发器，当检测到 DagBrain 有待分配事件时，触发责任链
     * @param evt 事件
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(ListenTopic.TRIGGER.equals(evt.getPropertyName())){
            TriggerContent content = (TriggerContent) evt.getNewValue();
            TaskExecution taskExecution = initTaskExecution(content.getJobId(), content.getDagNode().getId(), content.getDag());
            schedulerChain.run(taskExecution);
        }
    }

    private TaskExecution initTaskExecution(String jobId, String dagNodeId , DAG dag){
        SimpleTaskExecution execution = new SimpleTaskExecution();
        execution.setJobId(jobId);
        execution.setNode(dag.getNode(dagNodeId));
        execution.appendEnv(contextStore.get(jobId));
        execution.setId(SnowflakeIdGenerator.getInstance().nextIdStr());
        execution.setInput(dag.getNode(dagNodeId).getConfig());
        execution.setStatus(TaskStatus.CREATING);
        return execution;
    }
}
