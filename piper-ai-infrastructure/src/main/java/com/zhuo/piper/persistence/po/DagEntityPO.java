package com.zhuo.piper.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("dag")
public class DagEntityPO {
    private String id;

    private String parentId;

    private Long version;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
