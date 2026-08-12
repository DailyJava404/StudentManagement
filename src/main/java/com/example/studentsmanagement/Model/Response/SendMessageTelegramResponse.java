package com.example.studentsmanagement.Model.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SendMessageTelegramResponse(
        @JsonProperty("ok")
        Boolean success,

        @JsonProperty("result")
        Result result,

        @JsonProperty("error_code")
        Integer errorCode,

        @JsonProperty("description")
        String description
) {

    public record Result(
            @JsonProperty("message_id")
            Long resultMessageId,

            @JsonProperty("date")
            Long resultDate,

            @JsonProperty("chat")
            Chat resultChat,

            @JsonProperty("text")
            String resultText
    ) {
    }

    public record Chat(
            @JsonProperty("id")
            Long chatId,

            @JsonProperty("type")
            String chatType,

            @JsonProperty("title")
            String chatTitle
    ) {
    }
}


