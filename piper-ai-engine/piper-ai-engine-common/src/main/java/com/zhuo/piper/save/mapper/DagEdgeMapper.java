package com.zhuo.piper.save.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuo.piper.save.entity.DagEdge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DagEdgeMapper extends BaseMapper<DagEdge> {

    @Select("SELECT * FROM dag_edge WHERE from_node_id IN (SELECT id FROM dag_node WHERE dag_id = #{dagId})")
    List<DagEdge> selectByDagId(String dagId);
}
