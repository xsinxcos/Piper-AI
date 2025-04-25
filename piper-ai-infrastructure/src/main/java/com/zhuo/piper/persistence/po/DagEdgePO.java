package com.zhuo.piper.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("dag_edge")
public class DagEdgePO extends BasePO {
    private String id;

    private String fromNodeId;

    private String toNodeId;
}
