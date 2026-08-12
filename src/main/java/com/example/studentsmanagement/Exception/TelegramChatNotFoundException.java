package com.example.studentsmanagement.Exception;

public class TelegramChatNotFoundException extends TelegramApiException {

    public TelegramChatNotFoundException(Integer errorCode, String description) {
        super(errorCode, description);
    }
}
