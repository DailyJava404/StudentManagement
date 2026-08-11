package com.example.studentsmanagement.Controller;

import com.example.studentsmanagement.Model.Request.LoginRequest;
import com.example.studentsmanagement.Model.Request.RegisterRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.AuthResponse;
import com.example.studentsmanagement.Interface.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService _authService;
    public AuthController(IAuthService authService) {
        _authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse<AuthResponse> result = _authService.register(request);
        return ResponseEntity.status(result.statusCode()).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse<AuthResponse> result = _authService.login(request);
        return ResponseEntity.status(result.statusCode()).body(result);
    }
}
