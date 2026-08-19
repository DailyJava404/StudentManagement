package com.example.studentsmanagement.Controller;

import com.example.studentsmanagement.Model.Request.JsonPlaceholderRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.JsonPlaceholderResponse;
import com.example.studentsmanagement.Service.JsonPlaceholderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/external/users")
public class JsonPlaceholderController {
    private final JsonPlaceholderService jsonPlaceholderService;
    public JsonPlaceholderController(JsonPlaceholderService jsonPlaceholderService) {
        this.jsonPlaceholderService = jsonPlaceholderService;
    }

    @GetMapping()
    public CompletableFuture<ResponseEntity<ApiResponse<List<JsonPlaceholderResponse>>>> getUsers() {
        CompletableFuture<ApiResponse<List<JsonPlaceholderResponse>>> result = jsonPlaceholderService.getUsers();
        return result.thenApply(r -> ResponseEntity.status(r.statusCode()).body(r));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<JsonPlaceholderResponse>> createUser(@Valid @RequestBody JsonPlaceholderRequest request) {
        ApiResponse<JsonPlaceholderResponse> result = jsonPlaceholderService.createUser(request);
        return ResponseEntity.status(result.statusCode()).body(result);
    }
}
