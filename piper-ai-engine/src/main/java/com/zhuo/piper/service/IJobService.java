package com.zhuo.piper.service;

import com.zhuo.piper.model.entity.JobEntity;

import java.util.Optional;

public interface IJobService {

    Optional<JobEntity> createJob(String dagId);

    boolean updateRunning(String JobId);

    boolean updateSuccess(String JobId);
}
