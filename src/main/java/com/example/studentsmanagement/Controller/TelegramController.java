package com.example.studentsmanagement.Controller;

import com.example.studentsmanagement.Model.Request.SendMessageTelegramRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.SendMessageTelegramResponse;
import com.example.studentsmanagement.Service.TelegramService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/telegram")
public class TelegramController {

    private static final Logger logger = LoggerFactory.getLogger(TelegramController.class);
    private final ObjectMapper objectMapper;
    private final TelegramService telegramService;
    public TelegramController(ObjectMapper objectMapper, TelegramService telegramService) {
        this.objectMapper = objectMapper;
        this.telegramService = telegramService;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<SendMessageTelegramResponse>> sendMessage(@Valid @RequestBody SendMessageTelegramRequest request) {
        ApiResponse<SendMessageTelegramResponse> result = telegramService.sendMessage(request);
        return ResponseEntity.status(result.statusCode()).body(result);
    }

}
