package com.zhuo.piper.http.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.zhuo.piper.utils.IHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

/**
 * HTTP客户端工具类
 * 提供常用的HTTP请求方法，支持GET、POST、PUT、DELETE等操作
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class HttpClient implements IHttpClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = buildObjectMapper();

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
    /**
     * 发送GET请求
     *
     * @param path   请求路径
     * @param params 查询参数
     * @return 响应结果
     */
    public JsonNode get(String path, Map<String, String> params, String baseUrl) {
        String url = buildUrl(path, params, baseUrl);
        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return convertToJsonNode(response.getBody());
    }

    /**
     * 发送POST请求
     *
     * @param path 请求路径
     * @param body 请求体
     * @return 响应结果
     */
    public JsonNode post(String path, Object body, String baseUrl) {
        String url = buildUrl(path, null, baseUrl);
        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
        return convertToJsonNode(response.getBody());
    }

    /**
     * 发送PUT请求
     *
     * @param path 请求路径
     * @param body 请求体
     * @return 响应结果
     */
    public JsonNode put(String path, Object body, String baseUrl) {
        String url = buildUrl(path, null, baseUrl);
        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                String.class
        );

        return convertToJsonNode(response.getBody());
    }

    /**
     * 发送DELETE请求
     *
     * @param path 请求路径
     * @return 响应结果
     */
    public JsonNode delete(String path, String baseUrl) {
        String url = buildUrl(path, null, baseUrl);
        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                String.class
        );

        return convertToJsonNode(response.getBody());
    }

    /**
     * 发送表单数据
     *
     * @param path     请求路径
     * @param formData 表单数据
     * @return 响应结果
     */
    public JsonNode postForm(String path, Map<String, String> formData, String baseUrl) {
        String url = buildUrl(path, null, baseUrl);
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        formData.forEach(map::add);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        return convertToJsonNode(response.getBody());
    }

    /**
     * 构建完整的URL
     *
     * @param path   路径
     * @param params 参数
     * @return 完整URL
     */
    private String buildUrl(String path, Map<String, String> params, String baseUrl) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(path);

        if (params != null) {
            params.forEach(builder::queryParam);
        }

        return builder.build().toUriString();
    }

    /**
     * 创建HTTP请求头
     *
     * @return HTTP请求头
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    /**
     * 将响应字符串转换为JsonNode
     *
     * @param response 响应内容
     * @return JsonNode对象
     */
    private JsonNode convertToJsonNode(String response) {
        try {
            return objectMapper.readTree(response);
        } catch (Exception e) {
            log.error("响应转换失败", e);
            throw new RuntimeException("响应转换失败", e);
        }
    }
} 