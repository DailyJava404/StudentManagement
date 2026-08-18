package com.example.studentsmanagement.Exception;

import com.example.studentsmanagement.Model.Request.JsonPlaceholderRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.JsonPlaceholderResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class JsonPlaceholderFallbackHandler {
    public CompletableFuture<ApiResponse<List<JsonPlaceholderResponse>>> handleGetUsersFailure(Throwable t){
        return CompletableFuture.completedFuture(ApiResponse.fail(504,"Service temporarily unavailable"));
    }
}
