package com.zhuo.piper.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zhuo.piper.exception.EngineException;

import java.util.Optional;

/**
 * 任务上下文
 */
public class TaskJsonNodeContext implements ITaskContext<JsonNode> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private ObjectNode objectNode = null;

    private TaskJsonNodeContext(ObjectNode objectNode) {
        this.objectNode = objectNode;
    }

    public static TaskJsonNodeContext getInstance() {
        ObjectNode node = objectMapper.createObjectNode();
        return new TaskJsonNodeContext(node);
    }

    @Override
    public void put(String key, Object value) {
        Optional.ofNullable(objectNode).orElseThrow(() -> new EngineException("TaskJsonNodeContext 非法，请通过 getInstance 获取"));
        this.objectNode.set(key, (JsonNode) value);
    }

    @Override
    public JsonNode get(String key) {
        return objectNode.get(key);
    }
}
