package com.zhuo.piper.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;

public class JsonUtils {
    // 线程安全的ObjectMapper实例
    private static final ObjectMapper defaultMapper = buildObjectMapper();

    // 基础配置的ObjectMapper
    private static ObjectMapper buildObjectMapper() {
        return new ObjectMapper()
                // 禁用自动关闭流
                .disable(SerializationFeature.CLOSE_CLOSEABLE)
                // 禁用未知属性异常
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // 空值不参与序列化
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                // 使用标准日期格式
                .setDateFormat(new StdDateFormat().withColonInTimeZone(true))
                // 禁用日期转时间戳
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // 获取默认配置的ObjectMapper
    public static ObjectMapper getObjectMapper() {
        return defaultMapper;
    }

    // 快速生成自定义配置的ObjectMapper
    public static ObjectMapper createCustomMapper() {
        return buildObjectMapper().copy();
    }

    /**
     * 对象转JSON（使用默认配置）
     */
    public static String toJson(Object object) {
        try {
            return defaultMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    /**
     * 对象转JSON（带自定义配置）
     */
    public static String toJson(Object object, ObjectMapper customMapper) {
        try {
            return customMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    /**
     * 对象转JSON（安全版，返回Optional）
     */
    public static Optional<String> toJsonSafe(Object object) {
        try {
            return Optional.of(defaultMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    /**
     * 美化输出
     */
    public static String toPrettyJson(Object object) {
        try {
            return defaultMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON美化序列化失败", e);
        }
    }

    /**
     * 启用日期格式化（示例方法）
     */
    public static void enableDateFormat(String pattern) {
        defaultMapper.setDateFormat(new SimpleDateFormat(pattern));
    }

    /**
     * 启用空值序列化
     */
    public static void enableSerializeNulls() {
        defaultMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }

    /**
     * 字符串转JsonNode（默认配置）
     *
     * @throws RuntimeException 当解析失败时抛出
     */
    public static JsonNode toJsonNode(String jsonStr) {
        try {
            return defaultMapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON解析失败: " + e.getOriginalMessage(), e);
        }
    }

    /**
     * 安全转换（返回Optional）
     */
    public static Optional<JsonNode> toJsonNodeSafe(String jsonStr) {
        try {
            return Optional.of(defaultMapper.readTree(jsonStr));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    /**
     * 使用自定义ObjectMapper转换
     */
    public static JsonNode toJsonNode(String jsonStr, ObjectMapper customMapper) {
        try {
            return customMapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON解析失败: " + e.getOriginalMessage(), e);
        }
    }


    /**
     * 基础映射方法（使用默认ObjectMapper）
     *
     * @param jsonNode   要转换的JSON节点
     * @param targetType 目标类
     * @return 映射后的对象
     * @throws RuntimeException 包含具体的转换失败原因
     */
    public static <T> T fromJsonNode(JsonNode jsonNode, Class<T> targetType) {
        try {
            return defaultMapper.treeToValue(jsonNode, targetType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON解析失败: " + e.getOriginalMessage(), e);
        }
    }

    /**
     * 安全映射方法（返回Optional）
     */
    public static <T> Optional<T> fromJsonNodeSafe(JsonNode jsonNode, Class<T> targetType) {
        try {
            return Optional.of(defaultMapper.treeToValue(jsonNode, targetType));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    /**
     * 将 json (string) 转化为 指定对象
     */
    public static <T> T fromJsonNode(String jsonStr, Class<T> targetType) {
        try {
            return defaultMapper.readValue(jsonStr, targetType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON解析失败: " + e.getOriginalMessage(), e);
        }
    }
    /**
     * 将JSON字符串反序列化为Map<String, Object>
     */
    public static Map<String, Object> jsonToMap(String json) throws Exception {
        try {
            return defaultMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON to Map: " + e.getMessage(), e);
        }
    }

    /**
     * 从Map中提取指定键的值，并转换为目标类型
     */
    public static <T> T mapToObject(Map<String, Object> map, String key, Class<T> targetType) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        // 如果值是Map类型，需要特殊处理
        if (value instanceof Map) {
            return defaultMapper.convertValue(value, targetType);
        }
        // 如果值是String类型，需要先解析为JsonNode
        if (value instanceof String) {
            try {
                JsonNode jsonNode = defaultMapper.readTree((String) value);
                return defaultMapper.convertValue(jsonNode, targetType);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse JSON string", e);
            }
        }
        return defaultMapper.convertValue(value, targetType);
    }


}
