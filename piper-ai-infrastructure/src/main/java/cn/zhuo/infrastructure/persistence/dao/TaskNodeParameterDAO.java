package cn.zhuo.infrastructure.persistence.dao;

import cn.zhuo.infrastructure.persistence.po.TaskNodeParameterPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务节点参数数据访问对象
 */
@Mapper
public interface TaskNodeParameterDAO {

    /**
     * 插入任务节点参数
     *
     * @param po 任务节点参数持久化对象
     * @return 影响行数
     */
    int insert(TaskNodeParameterPO po);

    /**
     * 批量插入任务节点参数
     *
     * @param poList 任务节点参数持久化对象列表
     * @return 影响行数
     */
    int batchInsert(List<TaskNodeParameterPO> poList);

    /**
     * 更新任务节点参数
     *
     * @param po 任务节点参数持久化对象
     * @return 影响行数
     */
    int update(TaskNodeParameterPO po);

    /**
     * 根据ID查询任务节点参数
     *
     * @param id 参数ID
     * @return 任务节点参数持久化对象
     */
    TaskNodeParameterPO selectById(@Param("id") String id);

    /**
     * 根据节点ID查询所有参数
     *
     * @param nodeId 节点ID
     * @return 任务节点参数持久化对象列表
     */
    List<TaskNodeParameterPO> selectByNodeId(@Param("nodeId") String nodeId);

    /**
     * 根据节点ID和参数键查询参数
     *
     * @param nodeId   节点ID
     * @param paramKey 参数键
     * @return 任务节点参数持久化对象
     */
    TaskNodeParameterPO selectByNodeIdAndKey(@Param("nodeId") String nodeId, @Param("paramKey") String paramKey);

    /**
     * 删除任务节点参数
     *
     * @param id 参数ID
     * @return 影响行数
     */
    int deleteById(@Param("id") String id);

    /**
     * 根据节点ID删除所有参数
     *
     * @param nodeId 节点ID
     * @return 影响行数
     */
    int deleteByNodeId(@Param("nodeId") String nodeId);
} 