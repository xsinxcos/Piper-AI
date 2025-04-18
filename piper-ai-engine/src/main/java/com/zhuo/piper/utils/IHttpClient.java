package com.zhuo.piper.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface IHttpClient {
    JsonNode get(String path, Map<String, String> params, String baseUrl);

    JsonNode post(String path, Object body, String baseUrl);

    JsonNode put(String path, Object body, String baseUrl);

    JsonNode delete(String path, String baseUrl);

    JsonNode postForm(String path, Map<String, String> formData, String baseUrl);

}
