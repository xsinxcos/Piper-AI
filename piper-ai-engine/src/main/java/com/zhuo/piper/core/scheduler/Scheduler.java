package com.zhuo.piper.core.scheduler;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.core.scheduler.chain.after.JobSuccessHandler;
import com.zhuo.piper.core.scheduler.chain.before.AssignScheduler;
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

@Component
public class Scheduler {
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
    private AssignScheduler assignScheduler;
    @Resource
    private RunSubProcessScheduler runSubProcessScheduler;
    @Resource
    private JobStartHandler jobStartHandler;
    @Resource
    private JobSuccessHandler jobSuccessHandler;

    @PostConstruct
    void init() {
        jobStartHandler.setNext(assignScheduler);
        // 在还没遍历完所有节点时，责任链成环
        assignScheduler.setNext(taskExecutionInitHandler);
        assignScheduler.setEndChain(jobSuccessHandler);

        taskExecutionInitHandler.setNext(dynamicSchedule);

        dynamicSchedule.addNext("task", taskScheduler);
        dynamicSchedule.addNext("process", processScheduler);

        processScheduler.setNext(runSubProcessScheduler);

        runSubProcessScheduler.setNext(assignScheduler);

        schedulerChain = jobStartHandler;
    }


    public void run(DAG dag, TaskExecution execution) {
        schedulerChain.run(execution, dag);
    }
}
