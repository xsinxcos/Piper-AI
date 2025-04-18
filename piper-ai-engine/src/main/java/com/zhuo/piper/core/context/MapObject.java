package com.zhuo.piper.core.context;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.*;

public class MapObject implements Map<String, Object>, Accessor, Mutator {
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final ConversionService conversionService = DefaultConversionService.getSharedInstance();
    private final HashMap<String, Object> map;

    public MapObject() {
        map = new HashMap<>();
    }

    public MapObject(Map<String, Object> aSource) {
        map = new HashMap<String, Object>(aSource);
    }

    public static MapObject getInstance() {
        return new MapObject();
    }

    public static MapObject empty() {
        return new MapObject(Collections.emptyMap());
    }

    public static MapObject of(Map<String, Object> aMap) {
        return new MapObject(aMap);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object aKey) {
        return map.containsKey(aKey);
    }

    @Override
    public boolean containsValue(Object aValue) {
        return map.containsValue(aValue);
    }

    @Override
    public Object get(Object aKey) {
        return map.get(aKey);
    }

    @Override
    public String getString(Object aKey) {
        Object value = get(aKey);
        return conversionService.convert(value, String.class);
    }

    @Override
    public String getRequiredString(Object aKey) {
        String value = getString(aKey);
        Assert.notNull(value, "Unknown key: " + aKey);
        return value;
    }

    @Override
    public <T> T getRequired(Object aKey, Class<T> aValueType) {
        T value = get(aKey, aValueType);
        Assert.notNull(value, "Unknown key: " + aKey);
        return value;
    }

    @Override
    public String getString(Object aKey, String aDefault) {
        String value = getString(aKey);
        return value != null ? value : aDefault;
    }

    @Override
    public Object put(String aKey, Object aValue) {
        return map.put(aKey, aValue);
    }

    @Override
    public Object remove(Object aKey) {
        return map.remove(aKey);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> aVariables) {
        map.putAll(aVariables);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public <T> T get(Object aKey, Class<T> aReturnType) {
        Object value = get(aKey);
        if (value == null) {
            return null;
        }
        return conversionService.convert(value, aReturnType);
    }

    @Override
    public <T> T get(Object aKey, Class<T> aReturnType, T aDefaultValue) {
        Object value = get(aKey);
        if (value == null) {
            return aDefaultValue;
        }
        return conversionService.convert(value, aReturnType);
    }

    @Override
    public Long getLong(Object aKey) {
        return get(aKey, Long.class);
    }

    @Override
    public long getLong(Object aKey, long aDefaultValue) {
        return get(aKey, Long.class, aDefaultValue);
    }

    @Override
    public Double getDouble(Object aKey) {
        return get(aKey, Double.class);
    }

    @Override
    public Double getDouble(Object aKey, double aDefaultValue) {
        return get(aKey, Double.class, aDefaultValue);
    }

    @Override
    public Float getFloat(Object aKey) {
        return get(aKey, Float.class);
    }

    @Override
    public float getFloat(Object aKey, float aDefaultValue) {
        return get(aKey, Float.class, aDefaultValue);
    }

    @Override
    public Integer getInteger(Object aKey) {
        return get(aKey, Integer.class);
    }

    @Override
    public int getInteger(Object aKey, int aDefaultValue) {
        return get(aKey, Integer.class, aDefaultValue);
    }

    @Override
    public Boolean getBoolean(Object aKey) {
        return get(aKey, Boolean.class);
    }

    @Override
    public boolean getBoolean(Object aKey, boolean aDefaultValue) {
        Boolean value = getBoolean(aKey);
        return value != null ? value : aDefaultValue;
    }

    @Override
    public Map<String, Object> getMap(Object aKey) {
        Map<String, Object> value = (Map<String, Object>) get(aKey);
        if (value == null) {
            return null;
        }
        return Collections.unmodifiableMap(value);
    }

    @Override
    public Map<String, Object> getMap(Object aKey, Map<String, Object> aDefault) {
        Map<String, Object> value = getMap(aKey);
        return value != null ? value : aDefault;
    }

    @Override
    public Map<String, Object> asMap() {
        return Collections.unmodifiableMap(new HashMap<>(map));
    }

    @Override
    public Date getDate(Object aKey) {
        return get(aKey, Date.class);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public boolean equals(Object aObj) {
        return map.equals(aObj);
    }

    @Override
    public <T> T getRequired(Object aKey) {
        T value = (T) get(aKey);
        Assert.notNull(value, "Unknown key: " + aKey);
        return value;
    }

    @Override
    public Duration getDuration(Object aKey) {
        String value = getString(aKey);
        if (value == null) return null;
        return Duration.parse("PT" + value);
    }

    @Override
    public Duration getDuration(Object aKey, String aDefaultDuration) {
        Duration value = getDuration(aKey);
        return value != null ? value : Duration.parse("PT" + aDefaultDuration);
    }

    @Override
    public void set(String aKey, Object aValue) {
        put(aKey, aValue);
    }

    @Override
    public void setIfNull(String aKey, Object aValue) {
        if (get(aKey) == null) {
            set(aKey, aValue);
        }
    }

    @Override
    public long increment(String aKey) {
        Long counter = getLong(aKey);
        if (counter == null) {
            counter = 0L;
        }
        counter++;
        set(aKey, counter);
        return counter;
    }
}
