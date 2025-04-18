package com.zhuo.piper.model.entity;

import com.zhuo.piper.model.valobj.DagNodeClassNameVO;
import com.zhuo.piper.model.valobj.DagNodeConfigVO;
import com.zhuo.piper.model.valobj.DagNodeTypeVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DagNodeEntity {
    // id
    private String id;

    // dagId
    private String dagId;

    // 配置 json 格式
    private DagNodeConfigVO config;

    // [0 任务][1 流程]
    private DagNodeTypeVO type;

    private DagNodeClassNameVO nodeClass;
}
