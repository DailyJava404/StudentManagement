package com.example.studentsmanagement.Model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record SendMessageTelegramRequest(
        @JsonProperty("chat_id")
        @NotBlank(message = "chatId must not be empty")
        String chatId,

        @NotBlank(message = "text must not be empty")
        String text
) {}
