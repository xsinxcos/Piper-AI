package com.zhuo.piper.core.scheduler.execute.chain;

import com.zhuo.piper.core.context.DSL;
import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.drive.EventDrive;
import com.zhuo.piper.core.drive.Topic;
import com.zhuo.piper.core.drive.TopicMessage;
import com.zhuo.piper.core.scheduler.execute.AbstractSchedulerChain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TaskScheduler extends AbstractSchedulerChain {

    private final EventDrive eventDrive;


    @Override
    public void run(TaskExecution aTask) {
        String trace = aTask.getString(DSL.TRACE);
        Map<String, Object> map = Map.of(DSL.TASK_EXECUTION, aTask);
        Object output = eventDrive.schedule(TopicMessage.getInstance(Topic.START, trace, map));
        SimpleTaskExecution task = (SimpleTaskExecution) aTask;
        task.setOutput(output);
        handleNext(aTask);
    }
}
