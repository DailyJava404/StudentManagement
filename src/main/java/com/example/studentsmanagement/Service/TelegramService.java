package com.example.studentsmanagement.Service;

import com.example.studentsmanagement.Client.TelegramClient;
import com.example.studentsmanagement.Interface.ITelegramService;
import com.example.studentsmanagement.Model.Request.SendMessageTelegramRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.SendMessageTelegramResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class TelegramService implements ITelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final TelegramClient telegramClient;
    private static final Logger logger = LoggerFactory.getLogger(TelegramService.class);
    private final ObjectMapper objectMapper;

    public TelegramService(TelegramClient telegramClient, ObjectMapper objectMapper) {
        this.telegramClient = telegramClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public ApiResponse<SendMessageTelegramResponse> sendMessage(SendMessageTelegramRequest request) {
        if (request.chatId() == null || request.chatId().isBlank()) {
            logger.debug("Telegram chat id must not be empty : {}", objectMapper.writeValueAsString(request));
            throw new IllegalArgumentException("Telegram chat id must not be empty");
        }
        logger.debug("Request Before Call To Provider : {}", objectMapper.writeValueAsString(request));
        SendMessageTelegramResponse sendMessageTelegramResponse = telegramClient.sendMessage(botToken, request);
        logger.debug("Response Telegram Message : {}", objectMapper.writeValueAsString(sendMessageTelegramResponse));
        if (!sendMessageTelegramResponse.success())
            return ApiResponse.fail(sendMessageTelegramResponse.errorCode(), sendMessageTelegramResponse.description());
        return ApiResponse.success(sendMessageTelegramResponse);
    }
}
