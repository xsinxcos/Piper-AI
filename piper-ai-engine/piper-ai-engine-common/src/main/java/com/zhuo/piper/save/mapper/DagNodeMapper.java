package com.zhuo.piper.save.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuo.piper.save.entity.DagNode;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DagNodeMapper extends BaseMapper<DagNode> {
    
    @Select("SELECT * FROM dag_node WHERE dag_id = #{dagId}")
    List<DagNode> selectByDagId(String dagId);
}
