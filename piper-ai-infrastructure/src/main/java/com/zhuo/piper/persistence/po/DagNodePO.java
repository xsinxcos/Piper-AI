package com.zhuo.piper.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("dag_node")
public class DagNodePO extends BasePO {
    // id
    private String id;

    // dagId
    private String dagId;

    // 配置 json 格式
    private String config;

    // [0 任务][1 流程]
    private Integer type;

    private String nodeClass;
}
