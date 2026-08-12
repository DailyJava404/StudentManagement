package com.example.studentsmanagement.Config;

import com.example.studentsmanagement.Exception.TelegramErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class TelegramFeignConfig {
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return new TelegramErrorDecoder();
    }
}
