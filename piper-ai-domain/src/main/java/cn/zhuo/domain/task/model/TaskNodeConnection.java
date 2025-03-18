package cn.zhuo.domain.task.model;

import cn.zhuo.domain.common.util.SnowflakeIdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 任务节点连接定义实体
 * 定义节点之间的连接关系
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskNodeConnection {
    
    private String id;
    private String sourceNodeId;
    private String targetNodeId;
    private String connectionName;
    private String connectionType;
    private String conditionExpression;
    private Integer priority;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    /**
     * 创建新的节点连接定义
     */
    public static TaskNodeConnection create(String sourceNodeId, String targetNodeId, 
                                          String connectionName, String connectionType,
                                          String conditionExpression, Integer priority, 
                                          String createdBy) {
        TaskNodeConnection connection = new TaskNodeConnection();
        connection.id = SnowflakeIdGenerator.getInstance().nextIdStr();
        connection.sourceNodeId = sourceNodeId;
        connection.targetNodeId = targetNodeId;
        connection.connectionName = connectionName;
        connection.connectionType = connectionType;
        connection.conditionExpression = conditionExpression;
        connection.priority = priority;
        connection.enabled = true;
        connection.createdAt = LocalDateTime.now();
        connection.createdBy = createdBy;
        connection.updatedAt = LocalDateTime.now();
        connection.updatedBy = createdBy;
        return connection;
    }

    private TaskNodeConnection(String id, String sourceNodeId, String targetNodeId, 
                             String connectionName, String connectionType,
                             String conditionExpression, Integer priority, Boolean enabled,
                             LocalDateTime createdAt, String createdBy,
                             LocalDateTime updatedAt, String updatedBy) {
        this.id = id;
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
        this.connectionName = connectionName;
        this.connectionType = connectionType;
        this.conditionExpression = conditionExpression;
        this.priority = priority;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    /**
     * 更新连接信息
     */
    public void updateConnection(String connectionName, String connectionType,
                               String conditionExpression, Integer priority, String updatedBy) {
        this.connectionName = connectionName;
        this.connectionType = connectionType;
        this.conditionExpression = conditionExpression;
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = updatedBy;
    }

    /**
     * 启用连接
     */
    public void enable(String updatedBy) {
        if (!this.enabled) {
            this.enabled = true;
            this.updatedAt = LocalDateTime.now();
            this.updatedBy = updatedBy;
        }
    }

    /**
     * 禁用连接
     */
    public void disable(String updatedBy) {
        if (this.enabled) {
            this.enabled = false;
            this.updatedAt = LocalDateTime.now();
            this.updatedBy = updatedBy;
        }
    }
} 