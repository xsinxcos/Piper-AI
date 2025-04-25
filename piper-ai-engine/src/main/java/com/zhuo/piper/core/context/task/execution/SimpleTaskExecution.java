/*
 * Copyright 2016-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhuo.piper.core.context.task.execution;

import com.zhuo.piper.core.context.DSL;
import com.zhuo.piper.core.context.MapObject;
import com.zhuo.piper.core.task.TaskStatus;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleTaskExecution extends MapObject implements TaskExecution , Serializable {

    public SimpleTaskExecution() {
        this(Collections.emptyMap());
    }


    private SimpleTaskExecution(TaskExecution aSource) {
        this(aSource.asMap());
    }

    public SimpleTaskExecution(Map<String, Object> aSource) {
        super(aSource);
    }

    /**
     * Creates a mutation {@link SimpleTaskExecution} instance which
     * is a copy of a {@link TaskExecution}.
     *
     * @param aSource The {@link TaskExecution} instance to copy.
     * @return the new {@link SimpleTaskExecution}
     */
    public static SimpleTaskExecution of(TaskExecution aSource) {
        return new SimpleTaskExecution(aSource);
    }

    /**
     * Creates a {@link SimpleTaskExecution} instance for the given
     * Key-Value pair.
     *
     * @return The new {@link SimpleTaskExecution}.
     */
    public static SimpleTaskExecution of(String aKey, Object aValue) {
        return new SimpleTaskExecution(Collections.singletonMap(aKey, aValue));
    }

    /**
     * Creates a {@link SimpleTaskExecution} instance for the given Key-Value
     * map.
     *
     * @return The new {@link SimpleTaskExecution}.
     */
    public static SimpleTaskExecution of(Map<String, Object> aSource) {
        return new SimpleTaskExecution(aSource);
    }

    @Override
    public String getId() {
        return getString(DSL.ID);
    }

    public void setId(String id) {
        set(DSL.ID, id);
    }

    public void setJobId(String jobId){
        set(DSL.JOB_ID, jobId);
    }

    @Override
    public String getDagNodeId() {
        return getString(DSL.DAG_NODE_ID);
    }

    @Override
    public String getJobId() {
        return getString(DSL.JOB_ID);
    }

    public void setDagNodeId(String dagNodeId) {
        set(DSL.DAG_NODE_ID, dagNodeId);
    }

    @Override
    public String getParentId() {
        return getString(DSL.PARENT_ID);
    }

    public void setParentId(String parentId) {
        set(DSL.PARENT_ID, parentId);
    }

    @Override
    public TaskStatus getStatus() {
        String status = getString(DSL.STATUS);
        if(status == null) {
            return null;
        }
        return TaskStatus.valueOf(status);
    }

    public void setStatus(TaskStatus status) {
        set(DSL.STATUS, status);
    }

    @Override
    public Object getEnv() {
        return get(DSL.ENV);
    }

    @Override
    public Object getOutput() {
        return get(DSL.OUTPUT);
    }

    public void setOutput(Object output) {
        set(DSL.OUTPUT, output);
    }

    public void setEnv(Object output) {
        set(DSL.ENV, output);
    }

    public void appendEnv(Map<String, Object> env) {
        Map<String, Object> currentEnv = (Map<String, Object>) get(DSL.ENV);
        if (currentEnv == null) {
            currentEnv = new HashMap<>();
        }
        currentEnv.putAll(env);
        set(DSL.ENV, currentEnv);
    }

    @Override
    public Object getInput() {
        return get(DSL.INPUT);
    }

    public void setInput(Object input) {
        set(DSL.INPUT, input);
    }

    @Override
    public Date getCreateTime() {
        return getDate(DSL.CREATE_TIME);
    }

    public void setCreateTime(Date createTime) {
        set(DSL.CREATE_TIME, createTime);
    }

    @Override
    public Date getStartTime() {
        return getDate(DSL.START_TIME);
    }

    public void setStartTime(Date startTime) {
        set(DSL.START_TIME, startTime);
    }

    @Override
    public Date getEndTime() {
        return getDate(DSL.END_TIME);
    }

    public void setEndTime(Date endTime) {
        set(DSL.END_TIME, endTime);
    }

    @Override
    public Date getExecutionTime() {
        return getDate(DSL.EXECUTION_TIME);
    }

    public void setExecutionTime(long executionTime) {
        set(DSL.EXECUTION_TIME, executionTime);
    }
}
