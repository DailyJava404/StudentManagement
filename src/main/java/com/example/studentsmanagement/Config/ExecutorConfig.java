package com.example.studentsmanagement.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean(name = "taskExecutor", destroyMethod = "shutdown")
    public ExecutorService taskExecutor() {
        return Executors.newCachedThreadPool();
    }
}
