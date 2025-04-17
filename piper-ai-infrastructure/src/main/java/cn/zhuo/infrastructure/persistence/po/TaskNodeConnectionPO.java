package cn.zhuo.infrastructure.persistence.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务节点连接持久化对象
 */
@Data
public class TaskNodeConnectionPO {

    /**
     * 连接ID
     */
    private String id;

    /**
     * 源节点ID
     */
    private String sourceNodeId;

    /**
     * 目标节点ID
     */
    private String targetNodeId;

    /**
     * 连接名称
     */
    private String connectionName;

    /**
     * 连接类型
     */
    private String connectionType;

    /**
     * 条件表达式
     */
    private String conditionExpression;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 更新人
     */
    private String updatedBy;
} 