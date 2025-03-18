package cn.zhuo.domain.task.repository;

import cn.zhuo.domain.task.model.TaskNodeParameter;

import java.util.List;
import java.util.Optional;

/**
 * 任务节点参数仓储接口
 */
public interface TaskNodeParameterRepository {
    
    /**
     * 保存任务节点参数
     */
    void save(TaskNodeParameter taskNodeParameter);
    
    /**
     * 根据ID查找任务节点参数
     */
    Optional<TaskNodeParameter> findById(String id);
    
    /**
     * 根据节点ID查询参数列表
     */
    List<TaskNodeParameter> findByNodeId(String nodeId);
    
    /**
     * 根据节点ID列表查询所有相关参数
     */
    List<TaskNodeParameter> findByNodeIds(List<String> nodeIds);
    
    /**
     * 删除任务节点参数
     */
    void delete(String id);
    
    /**
     * 删除节点的所有参数
     */
    void deleteByNodeId(String nodeId);
} 