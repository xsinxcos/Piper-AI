package cn.zhuo.domain.task.repository;

import cn.zhuo.domain.task.model.TaskNodeConnection;

import java.util.List;
import java.util.Optional;

/**
 * 任务节点连接仓储接口
 */
public interface TaskNodeConnectionRepository {
    
    /**
     * 保存任务节点连接
     */
    void save(TaskNodeConnection taskNodeConnection);
    
    /**
     * 根据ID查找任务节点连接
     */
    Optional<TaskNodeConnection> findById(String id);
    
    /**
     * 根据源节点ID查询连接列表
     */
    List<TaskNodeConnection> findBySourceNodeId(String sourceNodeId);
    
    /**
     * 根据目标节点ID查询连接列表
     */
    List<TaskNodeConnection> findByTargetNodeId(String targetNodeId);
    
    /**
     * 查询指定节点的所有相关连接(作为源节点或目标节点)
     */
    List<TaskNodeConnection> findByNodeId(String nodeId);
    
    /**
     * 根据源节点ID列表和目标节点ID列表查询连接
     */
    List<TaskNodeConnection> findByNodeIds(List<String> nodeIds);
    
    /**
     * 删除任务节点连接
     */
    void delete(String id);
    
    /**
     * 删除与指定节点相关的所有连接
     */
    void deleteByNodeId(String nodeId);
} 