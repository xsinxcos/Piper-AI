package com.zhuo.piper.core.scheduler.execute;

import com.zhuo.piper.model.aggregates.DAG;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TriggerContent {
    private DAG dag;

    private DAG.DagNode dagNode;

    private String jobId;
}
