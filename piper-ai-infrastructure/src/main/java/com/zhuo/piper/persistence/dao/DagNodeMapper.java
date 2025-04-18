package com.zhuo.piper.persistence.dao;


import com.zhuo.piper.persistence.po.DagNodePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DagNodeMapper extends BaseMapper<DagNodePO> {

    @Select("SELECT * FROM dag_node WHERE dag_id = #{dagId}")
    List<DagNodePO> selectByDagId(String dagId);
}
