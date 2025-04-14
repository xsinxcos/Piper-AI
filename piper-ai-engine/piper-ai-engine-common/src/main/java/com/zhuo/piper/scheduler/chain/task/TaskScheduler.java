package com.zhuo.piper.scheduler.chain.task;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.drive.EventDrive;
import com.zhuo.piper.drive.Topic;
import com.zhuo.piper.drive.TopicMessage;
import com.zhuo.piper.scheduler.DAG;
import com.zhuo.piper.scheduler.Scheduler;
import com.zhuo.piper.utils.JsonUtils;


public class TaskScheduler implements Scheduler {

    private final EventDrive eventDrive;

    public TaskScheduler(EventDrive eventDrive) {
        this.eventDrive = eventDrive;
    }

    @Override
    public void run(TaskExecution aTask, DAG dag) {
        Object input = aTask.getInput();
        String trace = aTask.getString("trace");
        eventDrive.schedule(new TopicMessage(Topic.START.getTopic(), trace, JsonUtils.toJson(input)));
    }
}
