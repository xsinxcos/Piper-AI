package com.zhuo.piper.worker.impl;

import cn.zhuo.infrastructure.persistence.response.Result;
import com.zhuo.piper.context.task.execution.SimpleTaskExecution;
import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.drive.TopicMessage;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.task.Handler;
import com.zhuo.piper.task.HandlerFactory;
import com.zhuo.piper.utils.JsonUtils;
import com.zhuo.piper.worker.IWorker;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class ZkWorker implements IWorker {

    @Resource
    private HandlerFactory handlerFactory;
        
    @PostMapping("/start")
    public Result<String> start(@RequestBody TopicMessage topicMessage) throws Exception {
        String msg = topicMessage.getMsg();
        Map<String, Object> map = JsonUtils.jsonToMap(msg);

        SimpleTaskExecution execution = JsonUtils.mapToObject(map ,"TaskExecution" , SimpleTaskExecution.class);
        DAG dag = JsonUtils.mapToObject(map , "dag" , DAG.class);
        String className = dag.getNode(execution.getDagNodeId()).getClassName();
        return Result.okResult((String) handlerFactory.getInstance(className).handle(execution));
    }

    @Override
    public void init() {

    }

    @Override
    public void run(TaskExecution aTask , Handler<?> handler) {
        try {
            handler.handle(aTask);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
