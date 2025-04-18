package com.zhuo.piper.persistence.dao;

import com.zhuo.piper.persistence.po.LocalMessagePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LocalMessageMapper extends BaseMapper<LocalMessagePO> {

    @Update("UPDATE local_message SET status = 1 WHERE message_id = #{messageId}")
    void updateStatusSuccess(String messageId);

    @Update("UPDATE local_message SET status = 2, error_message = #{failureReason} WHERE message_id = #{messageId}")
    void updateStatusFailed(String messageId, String failureReason);

    @Select("SELECT * FROM local_message WHERE message_id = #{messageId}")
    LocalMessagePO selectByMessageId(String messageId);
}
