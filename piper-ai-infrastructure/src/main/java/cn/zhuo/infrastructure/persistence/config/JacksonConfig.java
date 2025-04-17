package cn.zhuo.infrastructure.persistence.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 配置类
 * 配置 ObjectMapper 以处理序列化问题
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
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
} 