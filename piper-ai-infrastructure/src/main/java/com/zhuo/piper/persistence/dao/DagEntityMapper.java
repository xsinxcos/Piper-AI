package com.zhuo.piper.persistence.dao;

import com.zhuo.piper.persistence.po.DagEntityPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DagEntityMapper extends BaseMapper<DagEntityPO> {

    @Select("SELECT * FROM dag WHERE id = #{id} ORDER BY version DESC LIMIT 1")
    DagEntityPO selectLatest(String id);

    @Select("SELECT * FROM dag WHERE id = #{id} ORDER BY version DESC LIMIT 1")
    DagEntityPO selectById(String id);
}
