package cn.zhuo.infrastructure.persistence.dao;

import cn.zhuo.infrastructure.persistence.po.TaskNodeDefinitionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务节点定义数据访问对象
 */
@Mapper
public interface TaskNodeDefinitionDAO {

    /**
     * 插入任务节点定义
     *
     * @param po 任务节点定义持久化对象
     * @return 影响行数
     */
    int insert(TaskNodeDefinitionPO po);

    /**
     * 更新任务节点定义
     *
     * @param po 任务节点定义持久化对象
     * @return 影响行数
     */
    int update(TaskNodeDefinitionPO po);

    /**
     * 根据ID查询任务节点定义
     *
     * @param id 节点ID
     * @return 任务节点定义持久化对象
     */
    TaskNodeDefinitionPO selectById(@Param("id") String id);

    /**
     * 根据节点类型查询任务节点定义列表
     *
     * @param nodeType 节点类型
     * @return 任务节点定义持久化对象列表
     */
    List<TaskNodeDefinitionPO> selectByNodeType(@Param("nodeType") String nodeType);

    /**
     * 查询所有启用的任务节点定义
     *
     * @return 任务节点定义持久化对象列表
     */
    List<TaskNodeDefinitionPO> selectAllEnabled();

    /**
     * 删除任务节点定义
     *
     * @param id 节点ID
     * @return 影响行数
     */
    int deleteById(@Param("id") String id);
} 