package com.zhuo.piper.scheduler;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.scheduler.chain.after.CheckEndScheduler;
import com.zhuo.piper.scheduler.chain.before.AssignScheduler;
import com.zhuo.piper.scheduler.chain.before.DynamicSchedule;
import com.zhuo.piper.scheduler.chain.before.TaskExecutionInit;
import com.zhuo.piper.scheduler.chain.process.ProcessScheduler;
import com.zhuo.piper.scheduler.chain.task.TaskScheduler;
import com.zhuo.piper.struct.DAG;
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

    @PostConstruct
    void init() {
        // 在还没遍历完所有节点时，责任链成环
        assignScheduler.setNext(taskExecutionInit);

        taskExecutionInit.setNext(dynamicSchedule);

        dynamicSchedule.addNext("0" ,taskScheduler);
        dynamicSchedule.addNext("1" ,processScheduler);

        processScheduler.setNext(checkEndScheduler);

        taskScheduler.setNext(checkEndScheduler);

        checkEndScheduler.setNext(assignScheduler);

        schedulerChain = assignScheduler;
    }

    void run(DAG dag , TaskExecution execution) {
        schedulerChain.run(execution , dag);
    }
}
