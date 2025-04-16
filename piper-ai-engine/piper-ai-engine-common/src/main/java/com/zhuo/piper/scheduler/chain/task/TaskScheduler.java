package com.zhuo.piper.scheduler.chain.task;

import com.zhuo.piper.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.drive.EventDrive;
import com.zhuo.piper.drive.Topic;
import com.zhuo.piper.drive.TopicMessage;
import com.zhuo.piper.scheduler.chain.AbstractSchedulerChain;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.utils.JsonUtils;
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
        Object output = eventDrive.schedule(new TopicMessage(Topic.START.getTopic(), trace, JsonUtils.toJson(map)));
        SimpleTaskExecution task = (SimpleTaskExecution) aTask;
        task.setOutput(output);
        handleNext(aTask , dag);
    }
}
