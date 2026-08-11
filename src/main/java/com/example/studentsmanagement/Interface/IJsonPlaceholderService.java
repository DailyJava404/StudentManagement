package com.example.studentsmanagement.Interface;

import com.example.studentsmanagement.Model.Request.JsonPlaceholderRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.JsonPlaceholderResponse;

import java.util.List;

public interface IJsonPlaceholder {
    ApiResponse<List<JsonPlaceholderResponse>> getUsers();
    ApiResponse<JsonPlaceholderResponse> createUser(JsonPlaceholderRequest request);
}
