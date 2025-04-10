package com.zhuo.piper.save.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("dag")
public class DagEntity {
    private String id;

    private Long version;
}
