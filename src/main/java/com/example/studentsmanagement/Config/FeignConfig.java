package com.example.studentsmanagement.Config;

import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Configuration
public class FeignConfig {
    private static final Logger logger = LoggerFactory.getLogger(FeignConfig.class);
    private static final Pattern BOT_TOKEN = Pattern.compile("/bot[^/]+/");

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            if (!logger.isDebugEnabled()) return;
            String fullUrl = requestTemplate.feignTarget().url() + requestTemplate.path();
            String method = requestTemplate.method();
            String body = requestTemplate.body() != null ?
                    new String(requestTemplate.body(), StandardCharsets.UTF_8) : "";
            logger.debug("Request To Provider url: [{}] {}, Request: {}",
                    method, BOT_TOKEN.matcher(fullUrl).replaceAll("/bot***/"), body);
        };
    }
}
