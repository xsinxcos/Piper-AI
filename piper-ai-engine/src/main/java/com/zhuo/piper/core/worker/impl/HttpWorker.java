package com.zhuo.piper.core.worker.impl;

import com.zhuo.piper.core.context.DSL;
import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.drive.TopicMessage;
import com.zhuo.piper.core.task.HandlerFactory;
import com.zhuo.piper.core.worker.IWorker;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.type.http.Result;
import com.zhuo.piper.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class HttpWorker implements IWorker {

    @Resource
    private HandlerFactory handlerFactory;

    @PostMapping("/start")
    public Result<String> start(@RequestBody TopicMessage topicMessage) {
        String msg = topicMessage.getMsg();
        try {
            Map<String, Object> map = JsonUtils.jsonToMap(msg);
            SimpleTaskExecution execution = JsonUtils.mapToObject(map, DSL.TASK_EXECUTION, SimpleTaskExecution.class);
            DAG.DagNode node = (DAG.DagNode) execution.getNode();
            String className = node.getClassName();
            return Result.okResult((String) handlerFactory.getInstance(className).handle(execution));
        } catch (Exception e) {
            throw new RuntimeException("Failed to process message: " + e.getMessage(), e);
        }
    }


}
