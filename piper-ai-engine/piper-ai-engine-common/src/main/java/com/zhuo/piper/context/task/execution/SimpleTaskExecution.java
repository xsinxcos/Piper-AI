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

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class SimpleTaskExecution implements TaskExecution {
    private MapObject mapObject;

    public SimpleTaskExecution(MapObject mapObject) {
        this.mapObject = mapObject;
    }

    public void setId(String id){
       mapObject.set(DSL.ID, id);
    }

    public void setParentId(String parentId){
       mapObject.set(DSL.PARENT_ID, parentId);
    }

    public void setStatus(TaskStatus status){
       mapObject.set(DSL.STATUS, status);
    }

    public void setOutput(Object output){
       mapObject.set(DSL.OUTPUT, output);
    }

    public void setInput(Object input){
       mapObject.set(DSL.INPUT, input);
    }

    public void setCreateTime(Date createTime){
       mapObject.set(DSL.CREATE_TIME, createTime);
    }

    public void setStartTime(Date startTime){
       mapObject.set(DSL.START_TIME, startTime);
    }

    public void setEndTime(Date endTime){
       mapObject.set(DSL.END_TIME, endTime);
    }

    public void setExecutionTime(long executionTime){
       mapObject.set(DSL.EXECUTION_TIME, executionTime);
    }

    @Override
    public String getId() {
        return mapObject.getString(DSL.ID);
    }

    @Override
    public String getParentId() {
        return mapObject.getString(DSL.PARENT_ID);
    }

    @Override
    public TaskStatus getStatus() {
        return mapObject.get(DSL.STATUS, TaskStatus.class);
    }

    @Override
    public Object getOutput() {
        return mapObject.get(DSL.OUTPUT);
    }

    @Override
    public Object getInput() {
        return mapObject.get(DSL.INPUT);
    }

    @Override
    public Date getCreateTime() {
        return mapObject.getDate(DSL.CREATE_TIME);
    }

    @Override
    public Date getStartTime() {
        return mapObject.getDate(DSL.START_TIME);
    }

    @Override
    public Date getEndTime() {
        return mapObject.getDate(DSL.END_TIME);
    }

    @Override
    public long getExecutionTime() {
        return mapObject.getLong(DSL.EXECUTION_TIME, 0L);
    }

    @Override
    public <T> T get(Object aKey) {
        return (T) mapObject.get(aKey);
    }

    @Override
    public boolean containsKey(Object aKey) {
        return mapObject.containsKey(aKey);
    }

    @Override
    public <T> T get(Object aKey, Class<T> aReturnType) {
        return mapObject.get(aKey, aReturnType);
    }

    @Override
    public <T> T get(Object aKey, Class<T> aReturnType, T aDefaultValue) {
        return mapObject.get(aKey, aReturnType, aDefaultValue);
    }

    @Override
    public String getString(Object aKey) {
        return mapObject.getString(aKey);
    }

    @Override
    public <T> T[] getArray(Object aKey, Class<T> aElementType) {
        return mapObject.getArray(aKey, aElementType);
    }

    @Override
    public Map<String, Object> getMap(Object aKey) {
        return mapObject.getMap(aKey);
    }

    @Override
    public Map<String, Object> getMap(Object aKey, Map<String, Object> aDefault) {
        return Map.of();
    }

    @Override
    public String getRequiredString(Object aKey) {
        return mapObject.getRequiredString(aKey);
    }

    @Override
    public <T> T getRequired(Object aKey) {
        return mapObject.getRequired(aKey);
    }

    @Override
    public <T> T getRequired(Object aKey, Class<T> aValueType) {
        return mapObject.getRequired(aKey, aValueType);
    }

    @Override
    public String getString(Object aKey, String aDefaultValue) {
        return mapObject.getString(aKey, aDefaultValue);
    }

    @Override
    public <T> List<T> getList(Object aKey, Class<T> aElementType) {
        return mapObject.getList(aKey, aElementType);
    }

    @Override
    public <T> List<T> getList(Object aKey, Class<T> aElementType, List<T> aDefaultValue) {
        return mapObject.getList(aKey, aElementType, aDefaultValue);
    }

    @Override
    public Long getLong(Object aKey) {
        return mapObject.getLong(aKey);
    }

    @Override
    public long getLong(Object aKey, long aDefaultValue) {
        return mapObject.getLong(aKey, aDefaultValue);
    }

    @Override
    public Double getDouble(Object aKey) {
        return mapObject.getDouble(aKey);
    }

    @Override
    public Double getDouble(Object aKey, double aDefaultValue) {
        return mapObject.getDouble(aKey, aDefaultValue);
    }

    @Override
    public Float getFloat(Object aKey) {
        return mapObject.getFloat(aKey);
    }

    @Override
    public float getFloat(Object aKey, float aDefaultValue) {
        return mapObject.getFloat(aKey, aDefaultValue);
    }

    @Override
    public Integer getInteger(Object aKey) {
        return mapObject.getInteger(aKey);
    }

    @Override
    public int getInteger(Object aKey, int aDefaultValue) {
        return mapObject.getInteger(aKey, aDefaultValue);
    }

    @Override
    public Boolean getBoolean(Object aKey) {
        return mapObject.getBoolean(aKey);
    }

    @Override
    public boolean getBoolean(Object aKey, boolean aDefaultValue) {
        return mapObject.getBoolean(aKey, aDefaultValue);
    }

    @Override
    public Date getDate(Object aKey) {
        return mapObject.getDate(aKey);
    }

    @Override
    public Duration getDuration(Object aKey) {
        return mapObject.getDuration(aKey);
    }

    @Override
    public Duration getDuration(Object aKey, String aDefaultDuration) {
        return mapObject.getDuration(aKey, aDefaultDuration);
    }

    @Override
    public Map<String, Object> asMap() {
        return mapObject.asMap();
    }
}
