package com.example.studentsmanagement.Controller;

import com.example.studentsmanagement.Model.Request.SendMessageTelegramRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.SendMessageTelegramResponse;
import com.example.studentsmanagement.Service.TelegramService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram")
public class TelegramController {

    private final TelegramService telegramService;
    public TelegramController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<SendMessageTelegramResponse>> sendMessage(@RequestBody SendMessageTelegramRequest request) {
        ApiResponse<SendMessageTelegramResponse> result = telegramService.sendMessage(request);
        return ResponseEntity.status(result.statusCode()).body(result);
    }

}
