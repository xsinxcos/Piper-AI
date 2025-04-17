package cn.zhuo.domain.task.model;

import cn.zhuo.domain.common.util.SnowflakeIdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 任务节点定义实体
 * 代表工作流中的一个任务节点定义
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE) // 用于JPA等框架
public class TaskNodeDefinition {

    private String id;
    private String name;
    private String description;
    private String nodeType;
    private String dslContent;
    private Boolean enabled;
    private Integer version;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    private TaskNodeDefinition(String id, String name, String description, String nodeType,
                               String dslContent, Boolean enabled, Integer version,
                               LocalDateTime createdAt, String createdBy,
                               LocalDateTime updatedAt, String updatedBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.nodeType = nodeType;
        this.dslContent = dslContent;
        this.enabled = enabled;
        this.version = version;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    /**
     * 创建新的任务节点定义
     */
    public static TaskNodeDefinition create(String name, String nodeType, String dslContent, String createdBy) {
        TaskNodeDefinition definition = new TaskNodeDefinition();
        definition.id = SnowflakeIdGenerator.getInstance().nextIdStr();
        definition.name = name;
        definition.description = "";
        definition.nodeType = nodeType;
        definition.dslContent = dslContent;
        definition.enabled = true;
        definition.version = 1;
        definition.createdAt = LocalDateTime.now();
        definition.createdBy = createdBy;
        definition.updatedAt = LocalDateTime.now();
        definition.updatedBy = createdBy;
        return definition;
    }

    /**
     * 重构任务节点定义（从存储数据中恢复）
     */
    public static TaskNodeDefinition reconstitute(String id, String name, String description, String nodeType,
                                                  String dslContent, Boolean enabled, Integer version,
                                                  LocalDateTime createdAt, String createdBy,
                                                  LocalDateTime updatedAt, String updatedBy) {
        TaskNodeDefinition definition = new TaskNodeDefinition();
        definition.id = id;
        definition.name = name;
        definition.description = description;
        definition.nodeType = nodeType;
        definition.dslContent = dslContent;
        definition.enabled = enabled;
        definition.version = version;
        definition.createdAt = createdAt;
        definition.createdBy = createdBy;
        definition.updatedAt = updatedAt;
        definition.updatedBy = updatedBy;
        return definition;
    }

    /**
     * 更新任务节点定义内容
     */
    public void updateContent(String name, String description, String dslContent, String updatedBy) {
        this.name = name;
        this.description = description;
        this.dslContent = dslContent;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = updatedBy;
        this.version = this.version + 1;
    }

    /**
     * 启用任务节点
     */
    public void enable(String updatedBy) {
        if (!this.enabled) {
            this.enabled = true;
            this.updatedAt = LocalDateTime.now();
            this.updatedBy = updatedBy;
        }
    }

    /**
     * 停用任务节点
     */
    public void disable(String updatedBy) {
        if (this.enabled) {
            this.enabled = false;
            this.updatedAt = LocalDateTime.now();
            this.updatedBy = updatedBy;
        }
    }
} 