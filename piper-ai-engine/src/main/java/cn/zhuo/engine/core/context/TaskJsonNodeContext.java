package cn.zhuo.engine.core.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.Assert;

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
        Assert.notNull(objectNode, "TaskJsonNodeContext 未初始化");
        this.objectNode.set(key, (JsonNode) value);
    }

    @Override
    public JsonNode get(String key) {
        return objectNode.get(key);
    }
}
