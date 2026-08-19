package com.example.studentsmanagement.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ExecutorConfig {

    @Bean(name = "taskExecutor", destroyMethod = "shutdown")
    public ExecutorService taskExecutor() {
        return new ThreadPoolExecutor(
                10,
                50,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
