package com.example.studentsmanagement.Interface;

import com.example.studentsmanagement.Model.Request.SendMessageTelegramRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.SendMessageTelegramResponse;

public interface ITelegramService {
    ApiResponse<SendMessageTelegramResponse> sendMessage(SendMessageTelegramRequest request);
}
