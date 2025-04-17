package cn.zhuo.domain.task.model;

import cn.zhuo.domain.common.util.SnowflakeIdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 任务节点参数定义实体
 * 定义节点所需的参数信息
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskNodeParameter {

    private String id;
    private String nodeId;
    private String paramName;
    private String paramKey;
    private String paramType;
    private String defaultValue;
    private String description;
    private Boolean required;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    private TaskNodeParameter(String id, String nodeId, String paramName, String paramKey,
                              String paramType, String defaultValue, String description,
                              Boolean required, Integer displayOrder,
                              LocalDateTime createdAt, String createdBy,
                              LocalDateTime updatedAt, String updatedBy) {
        this.id = id;
        this.nodeId = nodeId;
        this.paramName = paramName;
        this.paramKey = paramKey;
        this.paramType = paramType;
        this.defaultValue = defaultValue;
        this.description = description;
        this.required = required;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    /**
     * 创建新的节点参数定义
     */
    public static TaskNodeParameter create(String nodeId, String paramName, String paramKey,
                                           String paramType, String defaultValue, String description,
                                           Boolean required, Integer displayOrder, String createdBy) {
        TaskNodeParameter parameter = new TaskNodeParameter();
        parameter.id = SnowflakeIdGenerator.getInstance().nextIdStr();
        parameter.nodeId = nodeId;
        parameter.paramName = paramName;
        parameter.paramKey = paramKey;
        parameter.paramType = paramType;
        parameter.defaultValue = defaultValue;
        parameter.description = description;
        parameter.required = required;
        parameter.displayOrder = displayOrder;
        parameter.createdAt = LocalDateTime.now();
        parameter.createdBy = createdBy;
        parameter.updatedAt = LocalDateTime.now();
        parameter.updatedBy = createdBy;
        return parameter;
    }

    /**
     * 更新参数信息
     */
    public void updateParameter(String paramName, String paramType, String defaultValue,
                                String description, Boolean required, Integer displayOrder,
                                String updatedBy) {
        this.paramName = paramName;
        this.paramType = paramType;
        this.defaultValue = defaultValue;
        this.description = description;
        this.required = required;
        this.displayOrder = displayOrder;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = updatedBy;
    }
} 