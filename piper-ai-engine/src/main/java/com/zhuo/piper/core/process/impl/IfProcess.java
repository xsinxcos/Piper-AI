package com.zhuo.piper.core.process.impl;

import com.zhuo.piper.core.context.DSL;
import com.zhuo.piper.core.context.task.execution.TaskExecution;
import com.zhuo.piper.core.parser.Parser;
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

    private final Parser parser;

    @SneakyThrows
    @Override
    public Object run(TaskExecution aTask, DAG dag) {
        String input = (String) aTask.getInput();
        Map<String, Object> map = JsonUtils.jsonToMap(input);
        String subDagId = JsonUtils.mapToObject(map, DSL.SUB_DAG_ID, String.class);
        String condition = JsonUtils.mapToObject(map, DSL.CONDITION, String.class);
        String right = JsonUtils.mapToObject(map, "right", String.class);
        String parse = parser.parse(condition, aTask.getEnv());
        String id = aTask.getDagNodeId();
        Integer depth = dag.getNode(id).getDepth();
        // 逻辑判断是否满足条件
        // 子图展开
        if (parse.equals(right)) {
            dagService.loadSubDag(subDagId).ifPresent(item -> {
                item.getNodes().forEach((s, dagNode) -> dagNode.setDepth(depth + 1));
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
