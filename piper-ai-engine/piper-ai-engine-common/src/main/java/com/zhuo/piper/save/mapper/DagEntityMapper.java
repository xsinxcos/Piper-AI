package com.zhuo.piper.save.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuo.piper.save.entity.DagEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DagEntityMapper extends BaseMapper<DagEntity> {

    @Select("SELECT * FROM dag WHERE id = #{id} ORDER BY version DESC LIMIT 1")
    DagEntity selectLatest(String id);

    @Select("SELECT * FROM dag WHERE id = #{id} ORDER BY version DESC LIMIT 1")
    DagEntity selectById(String id);
}
