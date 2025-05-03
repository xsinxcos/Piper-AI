package com.zhuo.piper.model.entity;

import com.zhuo.piper.model.valobj.JobDagNodeVO;
import com.zhuo.piper.type.job.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobEntity {
    private String id;

    private JobDagNodeVO nodeVO;

    private JobStatus status;
}
