package com.zhuo.piper.save.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("dag_edge")
public class DagEdge {
    private String id;

    private String fromNodeId;

    private String toNodeId;

    private Long version;
}
