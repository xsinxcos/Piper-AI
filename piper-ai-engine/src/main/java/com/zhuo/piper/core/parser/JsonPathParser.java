package com.zhuo.piper.core.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuo.piper.utils.JsonUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JsonPathParser implements Parser{
    private static final Pattern PATH_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");
    private final ObjectMapper objectMapper;

    public JsonPathParser() {
        this.objectMapper = new ObjectMapper();
    }

    public String parse(String template, Object input) throws Exception {
        String jsonStr = JsonUtils.toJson(input);
        if (!template.contains("${")) {
            return template;
        }
        JsonNode rootNode = objectMapper.readTree(jsonStr);
        Matcher matcher = PATH_PATTERN.matcher(template);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String path = matcher.group(1);
            String value = getValueFromPath(rootNode, path);
            matcher.appendReplacement(result, value != null ? value : "");
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private String getValueFromPath(JsonNode node, String path) {
        String[] parts = path.split("\\.");
        JsonNode current = node;

        for (String part : parts) {
            if (current == null) {
                return null;
            }

            if (part.contains("[")) {
                // Handle array access
                String[] arrayParts = part.split("\\[");
                String fieldName = arrayParts[0];
                int index = Integer.parseInt(arrayParts[1].replace("]", ""));

                current = current.get(fieldName);
                if (current != null && current.isArray() && index < current.size()) {
                    current = current.get(index);
                } else {
                    return null;
                }
            } else {
                current = current.get(part);
            }
        }

        return current != null ? current.asText() : null;
    }
}
