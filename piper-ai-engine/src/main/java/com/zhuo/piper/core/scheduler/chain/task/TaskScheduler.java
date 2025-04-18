package com.zhuo.piper.core.scheduler.chain.task;

import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.drive.EventDrive;
import com.zhuo.piper.core.drive.Topic;
import com.zhuo.piper.core.drive.TopicMessage;
import com.zhuo.piper.core.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.model.aggregates.DAG;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TaskScheduler extends AbstractSchedulerChain {

    private final EventDrive eventDrive;

    public TaskScheduler(EventDrive eventDrive) {
        this.eventDrive = eventDrive;
    }


    @Override
    public void run(TaskExecution aTask, DAG dag) {
        String trace = aTask.getString("trace");
        Map<String, Object> map = Map.of("TaskExecution", aTask, "dag", dag);
        Object output = eventDrive.schedule(TopicMessage.getInstance(Topic.START, trace, map));
        SimpleTaskExecution task = (SimpleTaskExecution) aTask;
        task.setOutput(output);
        handleNext(aTask, dag);
    }
}
