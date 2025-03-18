package cn.zhuo.infrastructure.persistence.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务节点工作流持久化对象
 */
@Data
public class TaskNodeFlowPO {
    
    /**
     * 工作流ID
     */
    private String id;
    
    /**
     * 工作流名称
     */
    private String flowName;
    
    /**
     * 工作流描述
     */
    private String description;
    
    /**
     * 工作流类型
     */
    private String flowType;
    
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