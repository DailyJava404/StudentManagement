package com.example.studentsmanagement.Interface;

import com.example.studentsmanagement.Model.Request.LoginRequest;
import com.example.studentsmanagement.Model.Request.RegisterRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.AuthResponse;

public interface IAuthService {
    ApiResponse<AuthResponse> register(RegisterRequest request);
    ApiResponse<AuthResponse> login(LoginRequest request);
}
