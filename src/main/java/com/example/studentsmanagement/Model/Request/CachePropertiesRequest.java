package com.example.studentsmanagement.Model.Request;

import com.example.studentsmanagement.Entity.StudentInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "cache")
@Component
@Setter
@Getter
public class CachePropertiesRequest {
    private Map<String, Long> ttl = new HashMap<>();
}
