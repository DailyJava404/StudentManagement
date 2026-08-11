package com.example.studentsmanagement.Controller;

import com.example.studentsmanagement.Model.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestController {

    @GetMapping("/test/protected")
    public ResponseEntity<ApiResponse<String>> protectedEndpoint() {
        return ResponseEntity.ok(
                ApiResponse.success("You are authenticated!")
        );
    }
}