package com.example.studentsmanagement.Controller;

import com.example.studentsmanagement.Model.Request.JsonPlaceholderRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.JsonPlaceholderResponse;
import com.example.studentsmanagement.Service.JsonPlaceholderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/external/users")
public class JsonPlaceholderController {
    private final JsonPlaceholderService jsonPlaceholderService;
    public JsonPlaceholderController(JsonPlaceholderService jsonPlaceholderService) {
        this.jsonPlaceholderService = jsonPlaceholderService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<JsonPlaceholderResponse>>> getUsers() {
        ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers();
        return ResponseEntity.status(result.statusCode()).body(result);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<JsonPlaceholderResponse>> createUser(@Valid @RequestBody JsonPlaceholderRequest request) {
        ApiResponse<JsonPlaceholderResponse> result = jsonPlaceholderService.createUser(request);
        return ResponseEntity.status(result.statusCode()).body(result);
    }
}
