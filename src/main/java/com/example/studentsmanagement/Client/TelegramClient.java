package com.example.studentsmanagement.Client;

import com.example.studentsmanagement.Model.Request.SendMessageTelegramRequest;
import com.example.studentsmanagement.Model.Response.SendMessageTelegramResponse;
import com.example.studentsmanagement.Config.TelegramFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "telegramClient",
        url = "https://api.telegram.org",
        configuration = TelegramFeignConfig.class
)
public interface TelegramClient {

    @PostMapping("/bot{token}/sendMessage")
    SendMessageTelegramResponse sendMessage(
            @PathVariable("token") String token,
            @RequestBody SendMessageTelegramRequest request
    );
}
