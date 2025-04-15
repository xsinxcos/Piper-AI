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
package com.zhuo.piper.context.task.execution;

import com.zhuo.piper.context.DSL;
import com.zhuo.piper.context.MapObject;
import com.zhuo.piper.task.TaskStatus;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class SimpleTaskExecution extends MapObject implements TaskExecution {

    public SimpleTaskExecution () {
        this(Collections.emptyMap());
    }

    private SimpleTaskExecution (TaskExecution aSource) {
        this(aSource.asMap());
    }

    public SimpleTaskExecution (Map<String,Object> aSource) {
        super(aSource);
    }

    public void setId(String id){
        set(DSL.ID, id);
    }

    public void setParentId(String parentId){
        set(DSL.PARENT_ID, parentId);
    }

    public void setStatus(TaskStatus status){
        set(DSL.STATUS, status);
    }

    public void setOutput(Object output){
        set(DSL.OUTPUT, output);
    }

    public void setInput(Object input){
        set(DSL.INPUT, input);
    }

    public void setCreateTime(Date createTime){
        set(DSL.CREATE_TIME, createTime);
    }

    public void setStartTime(Date startTime){
       set(DSL.START_TIME, startTime);
    }

    public void setEndTime(Date endTime){
        set(DSL.END_TIME, endTime);
    }

    public void setExecutionTime(long executionTime){
        set(DSL.EXECUTION_TIME, executionTime);
    }

    @Override
    public String getId() {
        return getString(DSL.ID);
    }

    @Override
    public String getParentId() {
        return getString(DSL.PARENT_ID);
    }

    @Override
    public TaskStatus getStatus() {
        return TaskStatus.valueOf(getString(DSL.STATUS));
    }

    @Override
    public Object getOutput() {
        return get(DSL.OUTPUT);
    }

    @Override
    public Object getInput() {
        return get(DSL.INPUT);
    }

    @Override
    public Date getCreateTime() {
        return getDate(DSL.CREATE_TIME);
    }

    @Override
    public Date getStartTime() {
        return getDate(DSL.START_TIME);
    }

    @Override
    public Date getEndTime() {
        return getDate(DSL.END_TIME);
    }

    @Override
    public long getExecutionTime() {
        return getLong(DSL.EXECUTION_TIME);
    }

    /**
     * Creates a mutation {@link SimpleTaskExecution} instance which
     * is a copy of a {@link TaskExecution}.
     *
     * @param aSource
     *          The {@link TaskExecution} instance to copy.
     * @return the new {@link SimpleTaskExecution}
     */
    public static SimpleTaskExecution of (TaskExecution aSource) {
        return new SimpleTaskExecution(aSource);
    }

    /**
     * Creates a {@link SimpleTaskExecution} instance for the given
     * Key-Value pair.
     *
     * @return The new {@link SimpleTaskExecution}.
     */
    public static SimpleTaskExecution of (String aKey, Object aValue) {
        return new SimpleTaskExecution(Collections.singletonMap(aKey, aValue));
    }

    /**
     * Creates a {@link SimpleTaskExecution} instance for the given Key-Value
     * map.
     *
     * @return The new {@link SimpleTaskExecution}.
     */
    public static SimpleTaskExecution of (Map<String,Object> aSource) {
        return new SimpleTaskExecution(aSource);
    }
}
