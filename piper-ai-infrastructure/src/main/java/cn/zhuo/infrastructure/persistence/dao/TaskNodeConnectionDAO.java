package cn.zhuo.infrastructure.persistence.dao;

import cn.zhuo.infrastructure.persistence.po.TaskNodeConnectionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务节点连接数据访问对象
 */
@Mapper
public interface TaskNodeConnectionDAO {
    
    /**
     * 插入任务节点连接
     *
     * @param po 任务节点连接持久化对象
     * @return 影响行数
     */
    int insert(TaskNodeConnectionPO po);
    
    /**
     * 批量插入任务节点连接
     *
     * @param poList 任务节点连接持久化对象列表
     * @return 影响行数
     */
    int batchInsert(List<TaskNodeConnectionPO> poList);
    
    /**
     * 更新任务节点连接
     *
     * @param po 任务节点连接持久化对象
     * @return 影响行数
     */
    int update(TaskNodeConnectionPO po);
    
    /**
     * 根据ID查询任务节点连接
     *
     * @param id 连接ID
     * @return 任务节点连接持久化对象
     */
    TaskNodeConnectionPO selectById(@Param("id") String id);
    
    /**
     * 根据流程ID查询所有节点连接
     *
     * @param flowId 流程ID
     * @return 任务节点连接持久化对象列表
     */
    List<TaskNodeConnectionPO> selectByFlowId(@Param("flowId") String flowId);
    
    /**
     * 根据源节点ID查询连接
     *
     * @param sourceNodeId 源节点ID
     * @return 任务节点连接持久化对象列表
     */
    List<TaskNodeConnectionPO> selectBySourceNodeId(@Param("sourceNodeId") String sourceNodeId);
    
    /**
     * 根据目标节点ID查询连接
     *
     * @param targetNodeId 目标节点ID
     * @return 任务节点连接持久化对象列表
     */
    List<TaskNodeConnectionPO> selectByTargetNodeId(@Param("targetNodeId") String targetNodeId);
    
    /**
     * 删除任务节点连接
     *
     * @param id 连接ID
     * @return 影响行数
     */
    int deleteById(@Param("id") String id);
    
    /**
     * 根据流程ID删除所有节点连接
     *
     * @param flowId 流程ID
     * @return 影响行数
     */
    int deleteByFlowId(@Param("flowId") String flowId);
} 