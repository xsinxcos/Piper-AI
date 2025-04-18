package com.zhuo.piper.model.entity;

import com.zhuo.piper.model.valobj.DagEdgeFromToVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DagEdgeEntity {
    private String id;

    private DagEdgeFromToVO edge;
}
