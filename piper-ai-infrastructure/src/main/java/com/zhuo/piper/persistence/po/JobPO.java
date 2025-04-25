package com.zhuo.piper.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("job")
public class JobPO extends BasePO {
    private String id;

    private String DagId;

    private String dagNodeId;

    private Integer status;
}
