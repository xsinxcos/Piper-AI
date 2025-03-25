package cn.zhuo.infrastructure.persistence.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
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
public class HttpClient {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    /**
     * 构造函数
     *
     * @param baseUrl 基础URL
     */
    public HttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * 发送GET请求
     *
     * @param path 请求路径
     * @param params 查询参数
     * @param responseType 响应类型
     * @return 响应结果
     */
    public <T> T get(String path, Map<String, String> params, Class<T> responseType) {
        String url = buildUrl(path, params);
        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        ResponseEntity<T> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            responseType
        );
        
        return response.getBody();
    }
    
    /**
     * 发送POST请求
     *
     * @param path 请求路径
     * @param body 请求体
     * @param responseType 响应类型
     * @return 响应结果
     */
    public <T> T post(String path, Object body, Class<T> responseType) {
        String url = buildUrl(path, null);
        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        
        ResponseEntity<T> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            responseType
        );
        
        return response.getBody();
    }
    
    /**
     * 发送PUT请求
     *
     * @param path 请求路径
     * @param body 请求体
     * @param responseType 响应类型
     * @return 响应结果
     */
    public <T> T put(String path, Object body, Class<T> responseType) {
        String url = buildUrl(path, null);
        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        
        ResponseEntity<T> response = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            entity,
            responseType
        );
        
        return response.getBody();
    }
    
    /**
     * 发送DELETE请求
     *
     * @param path 请求路径
     * @param responseType 响应类型
     * @return 响应结果
     */
    public <T> T delete(String path, Class<T> responseType) {
        String url = buildUrl(path, null);
        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        ResponseEntity<T> response = restTemplate.exchange(
            url,
            HttpMethod.DELETE,
            entity,
            responseType
        );
        
        return response.getBody();
    }
    
    /**
     * 发送表单数据
     *
     * @param path 请求路径
     * @param formData 表单数据
     * @param responseType 响应类型
     * @return 响应结果
     */
    public <T> T postForm(String path, Map<String, String> formData, Class<T> responseType) {
        String url = buildUrl(path, null);
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        formData.forEach(map::add);
        
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        
        ResponseEntity<T> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            responseType
        );
        
        return response.getBody();
    }
    
    /**
     * 构建完整的URL
     *
     * @param path 路径
     * @param params 参数
     * @return 完整URL
     */
    private String buildUrl(String path, Map<String, String> params) {
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
     * 将响应转换为指定类型
     *
     * @param response 响应内容
     * @param typeReference 类型引用
     * @return 转换后的对象
     */
    public <T> T convertResponse(Object response, TypeReference<T> typeReference) {
        try {
            return objectMapper.convertValue(response, typeReference);
        } catch (Exception e) {
            log.error("响应转换失败", e);
            throw new RuntimeException("响应转换失败", e);
        }
    }
} 