package com.example.studentsmanagement.Exception;

import lombok.Getter;

@Getter
public class TelegramApiException extends RuntimeException
{
    private final Integer errorCode;
    private final String description;

    public TelegramApiException(Integer errorCode, String description)
    {
        super(description);
        this.errorCode = errorCode;
        this.description = description;
    }
}

