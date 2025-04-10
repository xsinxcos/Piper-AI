package com.zhuo.piper.save.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("dag_node")
public class DagNode {
    // id
    private String id;

    // dagId
    private String dagId;

    // 配置 json 格式
    private String config;

    // [0 任务][1 流程]
    private Integer type;

    private String nodeClass;

    private Long version;
}
