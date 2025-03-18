package cn.zhuo.domain.task.repository;

import cn.zhuo.domain.task.model.TaskNodeDefinition;

import java.util.List;
import java.util.Optional;

/**
 * 任务节点定义仓储接口
 */
public interface TaskNodeDefinitionRepository {
    
    /**
     * 保存任务节点定义
     */
    void save(TaskNodeDefinition taskNodeDefinition);
    
    /**
     * 根据ID查找任务节点定义
     */
    Optional<TaskNodeDefinition> findById(String id);
    
    /**
     * 根据节点类型查询任务节点定义列表
     */
    List<TaskNodeDefinition> findByNodeType(String nodeType);
    
    /**
     * 查询所有启用的任务节点定义
     */
    List<TaskNodeDefinition> findAllEnabled();
    
    /**
     * 删除任务节点定义
     */
    void delete(String id);
} 