package com.zhuo.piper.service;

import com.zhuo.piper.model.aggregates.DAG;

import java.util.Optional;

public interface IDagService {
    void save(DAG dag);

    Optional<DAG> load(String dagId);

    Optional<DAG> loadSubDag(String subDagId);

}
