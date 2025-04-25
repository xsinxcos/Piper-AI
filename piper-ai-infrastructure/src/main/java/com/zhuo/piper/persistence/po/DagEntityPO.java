package com.zhuo.piper.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("dag")
public class DagEntityPO extends BasePO{
    private String id;

    private String parentId;

}
