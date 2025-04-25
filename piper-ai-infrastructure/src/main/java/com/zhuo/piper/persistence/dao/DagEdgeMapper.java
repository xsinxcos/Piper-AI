package com.zhuo.piper.persistence.dao;

import com.zhuo.piper.persistence.po.DagEdgePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DagEdgeMapper extends BaseMapper<DagEdgePO> {

    @Select("SELECT * FROM dag_edge WHERE from_node_id IN (SELECT id FROM dag_node WHERE dag_id = #{dagId} AND del_flag = 0)")
    List<DagEdgePO> selectByDagId(String dagId);
}
