package cn.zhuo.infrastructure.persistence.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工作流与节点的关联持久化对象
 */
@Data
public class TaskFlowNodePO {

    /**
     * 关联ID
     */
    private String id;

    /**
     * 工作流ID
     */
    private String flowId;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
} 