package cn.zhuo.infrastructure.persistence.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务节点定义持久化对象
 */
@Data
public class TaskNodeDefinitionPO {

    /**
     * 节点定义ID
     */
    private String id;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点描述
     */
    private String description;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * DSL内容
     */
    private String dslContent;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 版本号
     */
    private Integer version;

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