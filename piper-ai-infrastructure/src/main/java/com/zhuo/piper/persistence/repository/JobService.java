package com.zhuo.piper.persistence.repository;

import com.zhuo.piper.model.entity.JobEntity;
import com.zhuo.piper.model.valobj.JobDagNodeVO;
import com.zhuo.piper.persistence.dao.JobMapper;
import com.zhuo.piper.persistence.po.JobPO;
import com.zhuo.piper.service.IJobService;
import com.zhuo.piper.type.job.JobStatus;
import com.zhuo.piper.utils.SnowflakeIdGenerator;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JobService implements IJobService {
    @Resource
    private JobMapper jobMapper;

    @Override
    public Optional<JobEntity> createJob(String dagId) {
        long id = SnowflakeIdGenerator.getInstance().nextId();
        JobPO jobPO = JobPO.builder()
                .id(String.valueOf(id))
                .dagId(dagId)
                .status(JobStatus.CREATE.getStatus())
                .build();
        int insert = jobMapper.insert(jobPO);
        if(insert == 1){
            return Optional.of(new JobEntity(jobPO.getId() ,
                    new JobDagNodeVO(dagId) ,JobStatus.CREATE));
        }
        return Optional.empty();
    }

    @Override
    public boolean updateRunning(String jobId) {
        jobMapper.updateStatusRunning(jobId);
        return true;
    }

    @Override
    public boolean updateSuccess(String jobId) {
        jobMapper.updateStatusSuccess(jobId);
        return true;
    }
}
