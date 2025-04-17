package cn.zhuo.domain.task.model;

import cn.zhuo.domain.common.util.SnowflakeIdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务节点工作流定义实体
 * 定义由多个节点组成的完整流程
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskNodeFlow {

    private String id;
    private String flowName;
    private String description;
    private String flowType;
    private Boolean enabled;
    private Integer version;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    private List<String> nodeIds = new ArrayList<>();

    private TaskNodeFlow(String id, String flowName, String description, String flowType,
                         Boolean enabled, Integer version,
                         LocalDateTime createdAt, String createdBy,
                         LocalDateTime updatedAt, String updatedBy,
                         List<String> nodeIds) {
        this.id = id;
        this.flowName = flowName;
        this.description = description;
        this.flowType = flowType;
        this.enabled = enabled;
        this.version = version;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.nodeIds = nodeIds;
    }

    /**
     * 创建新的任务节点工作流
     */
    public static TaskNodeFlow create(String flowName, String flowType, String description, String createdBy) {
        TaskNodeFlow flow = new TaskNodeFlow();
        flow.id = SnowflakeIdGenerator.getInstance().nextIdStr();
        flow.flowName = flowName;
        flow.description = description;
        flow.flowType = flowType;
        flow.enabled = true;
        flow.version = 1;
        flow.createdAt = LocalDateTime.now();
        flow.createdBy = createdBy;
        flow.updatedAt = LocalDateTime.now();
        flow.updatedBy = createdBy;
        flow.nodeIds = new ArrayList<>();
        return flow;
    }

    /**
     * 更新工作流基本信息
     */
    public void updateBasicInfo(String flowName, String description, String flowType, String updatedBy) {
        this.flowName = flowName;
        this.description = description;
        this.flowType = flowType;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = updatedBy;
        this.version = this.version + 1;
    }

    /**
     * 添加节点到工作流
     */
    public void addNode(String nodeId) {
        if (!this.nodeIds.contains(nodeId)) {
            this.nodeIds.add(nodeId);
        }
    }

    /**
     * 从工作流中移除节点
     */
    public void removeNode(String nodeId) {
        this.nodeIds.remove(nodeId);
    }

    /**
     * 启用工作流
     */
    public void enable(String updatedBy) {
        if (!this.enabled) {
            this.enabled = true;
            this.updatedAt = LocalDateTime.now();
            this.updatedBy = updatedBy;
        }
    }

    /**
     * 禁用工作流
     */
    public void disable(String updatedBy) {
        if (this.enabled) {
            this.enabled = false;
            this.updatedAt = LocalDateTime.now();
            this.updatedBy = updatedBy;
        }
    }
} 