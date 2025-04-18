package com.zhuo.piper.core.process.impl;

import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.process.Process;
import com.zhuo.piper.core.process.ProcessType;
import com.zhuo.piper.model.aggregates.DAG;
import com.zhuo.piper.service.IDagService;
import com.zhuo.piper.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class IfProcess implements Process {

    private final IDagService dagService;

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
