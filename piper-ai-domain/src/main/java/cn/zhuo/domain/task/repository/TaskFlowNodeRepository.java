package cn.zhuo.domain.task.repository;

import cn.zhuo.domain.task.model.TaskFlowNode;

import java.util.List;
import java.util.Optional;

/**
 * 工作流与节点关联仓储接口
 */
public interface TaskFlowNodeRepository {
    
    /**
     * 保存工作流节点关联
     */
    void save(TaskFlowNode taskFlowNode);
    
    /**
     * 根据ID查找工作流节点关联
     */
    Optional<TaskFlowNode> findById(String id);
    
    /**
     * 根据工作流ID查询关联的所有节点ID列表
     */
    List<String> findNodeIdsByFlowId(String flowId);
    
    /**
     * 根据节点ID查询关联的所有工作流ID列表
     */
    List<String> findFlowIdsByNodeId(String nodeId);
    
    /**
     * 检查节点是否存在于特定工作流中
     */
    boolean existsByFlowIdAndNodeId(String flowId, String nodeId);
    
    /**
     * 删除特定关联
     */
    void delete(String id);
    
    /**
     * 删除工作流的所有节点关联
     */
    void deleteByFlowId(String flowId);
    
    /**
     * 删除特定节点的所有工作流关联
     */
    void deleteByNodeId(String nodeId);
    
    /**
     * 删除特定工作流和节点的关联
     */
    void deleteByFlowIdAndNodeId(String flowId, String nodeId);
} 