package com.zhuo.piper.core.scheduler;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.core.scheduler.chain.after.CheckEndScheduler;
import com.zhuo.piper.core.scheduler.chain.before.AssignScheduler;
import com.zhuo.piper.core.scheduler.chain.before.DynamicSchedule;
import com.zhuo.piper.core.scheduler.chain.before.TaskExecutionInit;
import com.zhuo.piper.core.scheduler.chain.process.ProcessScheduler;
import com.zhuo.piper.core.scheduler.chain.process.RunSubProcessScheduler;
import com.zhuo.piper.core.scheduler.chain.task.LocalMessageFirstScheduler;
import com.zhuo.piper.core.scheduler.chain.task.LocalMessageSecondScheduler;
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
    private TaskExecutionInit taskExecutionInit;
    @Resource
    private AssignScheduler assignScheduler;
    @Resource
    private CheckEndScheduler checkEndScheduler;
    @Resource
    private RunSubProcessScheduler runSubProcessScheduler;
    @Resource
    private LocalMessageFirstScheduler localMessageFirstScheduler;
    @Resource
    private LocalMessageSecondScheduler localMessageSecondScheduler;

    @PostConstruct
    void init() {
        // 在还没遍历完所有节点时，责任链成环
        assignScheduler.setNext(taskExecutionInit);
        assignScheduler.addCheckScheduler(checkEndScheduler);

        taskExecutionInit.setNext(dynamicSchedule);


        dynamicSchedule.addNext("0", taskScheduler);
        dynamicSchedule.addNext("1", processScheduler);

        processScheduler.setNext(runSubProcessScheduler);

        runSubProcessScheduler.setNext(assignScheduler);

        checkEndScheduler.setNext(assignScheduler);

        schedulerChain = assignScheduler;
    }

    public void run(DAG dag, TaskExecution execution) {
        schedulerChain.run(execution, dag);
    }
}
