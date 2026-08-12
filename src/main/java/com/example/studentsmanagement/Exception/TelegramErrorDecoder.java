package com.example.studentsmanagement.Exception;

import com.example.studentsmanagement.Interface.IErrorDecoder;
import com.example.studentsmanagement.Model.Response.SendMessageTelegramResponse;
import com.example.studentsmanagement.Service.JsonPlaceholderService;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TelegramErrorDecoder implements IErrorDecoder, ErrorDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(TelegramErrorDecoder.class);

    @Override
    public Exception decode(String method, Response response) {
        String description = "Unrecognized Telegram error response";
        Integer errorCode = response.status();
        try {
            String readBody = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            logger.debug("Response Telegram Message: {}", readBody);
            SendMessageTelegramResponse sendMessageTelegramResponse = objectMapper.readValue(readBody, SendMessageTelegramResponse.class);
            errorCode = sendMessageTelegramResponse.errorCode();
            description = sendMessageTelegramResponse.description();

        } catch (IOException e) {
            return new TelegramApiException(errorCode, "Failed to parse Telegram error response");
        }
        if (description != null && description.toLowerCase().contains("chat not found")) {
            return new TelegramChatNotFoundException(errorCode, description);
        }

        return new TelegramApiException(errorCode, description);

    }
}
