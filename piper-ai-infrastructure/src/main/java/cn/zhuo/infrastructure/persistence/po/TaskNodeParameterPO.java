package cn.zhuo.infrastructure.persistence.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务节点参数持久化对象
 */
@Data
public class TaskNodeParameterPO {
    
    /**
     * 参数ID
     */
    private String id;
    
    /**
     * 节点定义ID
     */
    private String nodeId;
    
    /**
     * 参数名称
     */
    private String paramName;
    
    /**
     * 参数键
     */
    private String paramKey;
    
    /**
     * 参数类型
     */
    private String paramType;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 参数描述
     */
    private String description;
    
    /**
     * 是否必填
     */
    private Boolean required;
    
    /**
     * 显示顺序
     */
    private Integer displayOrder;
    
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