package cn.zhuo.domain.task.repository;

import cn.zhuo.domain.task.model.TaskNodeFlow;

import java.util.List;
import java.util.Optional;

/**
 * 任务节点工作流仓储接口
 */
public interface TaskNodeFlowRepository {

    /**
     * 保存任务节点工作流
     */
    void save(TaskNodeFlow taskNodeFlow);

    /**
     * 根据ID查找任务节点工作流
     */
    Optional<TaskNodeFlow> findById(String id);

    /**
     * 根据流程类型查询工作流列表
     */
    List<TaskNodeFlow> findByFlowType(String flowType);

    /**
     * 查询所有启用的工作流
     */
    List<TaskNodeFlow> findAllEnabled();

    /**
     * 查询包含指定节点的所有工作流
     */
    List<TaskNodeFlow> findByNodeId(String nodeId);

    /**
     * 删除任务节点工作流
     */
    void delete(String id);
} 