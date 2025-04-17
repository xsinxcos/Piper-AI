package com.zhuo.piper.save.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuo.piper.save.entity.LocalMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LocalMessageMapper extends BaseMapper<LocalMessage> {

    @Update("UPDATE local_message SET status = 1 WHERE messageId = #{messageId}")
    void updateStatusSuccess(String messageId);

    @Update("UPDATE local_message SET status = 2 WHERE messageId = #{messageId} AND failureReason = #{failureReason}")
    void updateStatusFailed(String messageId, String failureReason);

    @Select("SELECT * FROM local_message WHERE messageId = #{messageId}")
    LocalMessage selectByMessageId(String messageId);
}
