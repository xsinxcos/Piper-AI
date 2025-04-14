package com.zhuo.piper.scheduler;

import com.zhuo.piper.context.MapObject;
import com.zhuo.piper.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.drive.EventDrive;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.scheduler.chain.before.ParallelSchedule;
import com.zhuo.piper.scheduler.chain.before.TaskExecutionInit;
import com.zhuo.piper.scheduler.chain.process.ProcessScheduler;
import com.zhuo.piper.scheduler.chain.task.TaskScheduler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
public class SchedulerCore {
    @Resource
    private EventDrive eventDrive;

    @Autowired
    @Qualifier(value = "AsyncExecutor")
    private Executor executor;

    private AbstractSchedulerChain schedulerChain;

    @PostConstruct
    void init() {
        TaskExecutionInit init = new TaskExecutionInit();
        init.setNext(
                new ParallelSchedule(executor,
                        new ProcessScheduler(),
                        new TaskScheduler(eventDrive))
        );
        schedulerChain.setNext(init);
    }

    void run(){
        schedulerChain.run(new SimpleTaskExecution(new MapObject()) ,new DAG());
    }
}
