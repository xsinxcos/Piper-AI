package com.zhuo.piper.save.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuo.piper.save.entity.DagEntity;
import org.apache.ibatis.annotations.Select;

public interface DagEntityMapper extends BaseMapper<DagEntity> {
    
    @Select("SELECT * FROM dag ORDER BY version DESC LIMIT 1")
    DagEntity selectLatest();
}
