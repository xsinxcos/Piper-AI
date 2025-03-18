package cn.zhuo.infrastructure.persistence.dao;

import cn.zhuo.infrastructure.persistence.po.TaskFlowNodePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务流程节点数据访问对象
 */
@Mapper
public interface TaskFlowNodeDAO {
    
    /**
     * 插入任务流程节点
     *
     * @param po 任务流程节点持久化对象
     * @return 影响行数
     */
    int insert(TaskFlowNodePO po);
    
    /**
     * 批量插入任务流程节点
     *
     * @param poList 任务流程节点持久化对象列表
     * @return 影响行数
     */
    int batchInsert(List<TaskFlowNodePO> poList);
    
    /**
     * 更新任务流程节点
     *
     * @param po 任务流程节点持久化对象
     * @return 影响行数
     */
    int update(TaskFlowNodePO po);
    
    /**
     * 根据ID查询任务流程节点
     *
     * @param id 节点ID
     * @return 任务流程节点持久化对象
     */
    TaskFlowNodePO selectById(@Param("id") String id);
    
    /**
     * 根据流程ID查询所有流程节点
     *
     * @param flowId 流程ID
     * @return 任务流程节点持久化对象列表
     */
    List<TaskFlowNodePO> selectByFlowId(@Param("flowId") String flowId);
    
    /**
     * 删除任务流程节点
     *
     * @param id 节点ID
     * @return 影响行数
     */
    int deleteById(@Param("id") String id);
    
    /**
     * 根据流程ID删除所有流程节点
     *
     * @param flowId 流程ID
     * @return 影响行数
     */
    int deleteByFlowId(@Param("flowId") String flowId);
} 