package com.zhuo.piper.core.worker.impl;

import com.zhuo.piper.core.context.DSL;
import com.zhuo.piper.core.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.drive.RpcClient;
import com.zhuo.piper.core.drive.TopicMessage;
import com.zhuo.piper.core.task.Handler;
import com.zhuo.piper.core.task.HandlerFactory;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.type.http.Result;
import com.zhuo.piper.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Map;

@DubboService
public class DubboWorker implements RpcClient {

    @Resource
    private HandlerFactory handlerFactory;

    @Override
    public Result<String> processMessage(TopicMessage topicMessage) {
        String msg = topicMessage.getMsg();
        try {
            Map<String, Object> map = JsonUtils.jsonToMap(msg);
            SimpleTaskExecution execution = JsonUtils.mapToObject(map, DSL.TASK_EXECUTION, SimpleTaskExecution.class);
            DAG dag = JsonUtils.mapToObject(map, DSL.DAG, DAG.class);
            String className = dag.getNode(execution.getDagNodeId()).getClassName();
            return Result.okResult((String) handlerFactory.getInstance(className).handle(execution));
        } catch (Exception e) {
            throw new RuntimeException("Failed to process message: " + e.getMessage(), e);
        }
    }


    public void run(TaskExecution aTask, Handler<?> handler) {
        try {
            handler.handle(aTask);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
} 