package cn.zhuo.infrastructure.persistence.dao;

import cn.zhuo.infrastructure.persistence.po.TaskNodeFlowPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务节点流程数据访问对象
 */
@Mapper
public interface TaskNodeFlowDAO {
    
    /**
     * 插入任务节点流程
     *
     * @param po 任务节点流程持久化对象
     * @return 影响行数
     */
    int insert(TaskNodeFlowPO po);
    
    /**
     * 更新任务节点流程
     *
     * @param po 任务节点流程持久化对象
     * @return 影响行数
     */
    int update(TaskNodeFlowPO po);
    
    /**
     * 根据ID查询任务节点流程
     *
     * @param id 流程ID
     * @return 任务节点流程持久化对象
     */
    TaskNodeFlowPO selectById(@Param("id") String id);
    
    /**
     * 根据名称模糊查询任务节点流程
     *
     * @param name 流程名称
     * @return 任务节点流程持久化对象列表
     */
    List<TaskNodeFlowPO> selectByNameLike(@Param("name") String name);
    
    /**
     * 查询所有任务节点流程
     *
     * @return 任务节点流程持久化对象列表
     */
    List<TaskNodeFlowPO> selectAll();
    
    /**
     * 删除任务节点流程
     *
     * @param id 流程ID
     * @return 影响行数
     */
    int deleteById(@Param("id") String id);
} 