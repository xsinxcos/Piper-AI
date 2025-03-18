package cn.zhuo.domain.task.model;

import cn.zhuo.domain.common.util.SnowflakeIdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 工作流与节点的关联实体
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskFlowNode {
    
    private String id;
    private String flowId;
    private String nodeId;
    private LocalDateTime createdAt;

    /**
     * 创建工作流节点关联
     */
    public static TaskFlowNode create(String flowId, String nodeId) {
        TaskFlowNode flowNode = new TaskFlowNode();
        flowNode.id = SnowflakeIdGenerator.getInstance().nextIdStr();
        flowNode.flowId = flowId;
        flowNode.nodeId = nodeId;
        flowNode.createdAt = LocalDateTime.now();
        return flowNode;
    }

    private TaskFlowNode(String id, String flowId, String nodeId, LocalDateTime createdAt) {
        this.id = id;
        this.flowId = flowId;
        this.nodeId = nodeId;
        this.createdAt = createdAt;
    }
} 