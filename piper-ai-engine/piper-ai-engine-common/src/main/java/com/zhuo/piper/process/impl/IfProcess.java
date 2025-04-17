package com.zhuo.piper.process.impl;

import com.zhuo.piper.context.task.execution.TaskExecution;
import com.zhuo.piper.process.Process;
import com.zhuo.piper.process.ProcessType;
import com.zhuo.piper.save.DagService;
import com.zhuo.piper.struct.DAG;
import com.zhuo.piper.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IfProcess implements Process {

    @Resource
    private DagService dagService;

    @SneakyThrows
    @Override
    public Object run(TaskExecution aTask, DAG dag) {
        String input = (String) aTask.getInput();
        Map<String, Object> map = JsonUtils.jsonToMap(input);
        String subDagId = JsonUtils.mapToObject(map, "subDagId", String.class);
        boolean condition = JsonUtils.mapToObject(map, "condition", Boolean.class);
        String id = aTask.getDagNodeId();
        // 逻辑判断是否满足条件
        // 子图展开
        if (condition) {
            dagService.loadSubDag(subDagId).ifPresent(item -> {
                dag.insertDAGAfterNode(id, item);
            });
        }
        return null;
    }

    @Override
    public ProcessType type() {
        return ProcessType.IF;
    }
}
