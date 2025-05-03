package com.zhuo.piper.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuo.piper.persistence.po.JobPO;
import org.apache.ibatis.annotations.Update;

public interface JobMapper extends BaseMapper<JobPO> {

    @Update("UPDATE job SET status = 1 WHERE id = #{jobId} AND del_flag = 0")
    void updateStatusRunning(String jobId);

    @Update("UPDATE job SET status = 2 WHERE id = #{jobId} AND del_flag = 0")
    void updateStatusSuccess(String jobId);
}
