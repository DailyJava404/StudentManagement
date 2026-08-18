package com.example.studentsmanagement.Service;

import com.example.studentsmanagement.Client.JsonPlaceholderClient;
import com.example.studentsmanagement.Exception.JsonPlaceholderFallbackHandler;
import com.example.studentsmanagement.Interface.IJsonPlaceholderService;
import com.example.studentsmanagement.Model.Request.JsonPlaceholderRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.JsonPlaceholderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class JsonPlaceholderService implements IJsonPlaceholderService {

    private final JsonPlaceholderClient jsonPlaceholderClient;
    private final ObjectMapper objectMapper;
    private final JsonPlaceholderFallbackHandler jsonPlaceholderFallbackHandler;
    private final ExecutorService taskExecutor;
    private static final Logger logger = LoggerFactory.getLogger(JsonPlaceholderService.class);

    public JsonPlaceholderService(JsonPlaceholderClient jsonPlaceholderClient,
                                  ObjectMapper objectMapper,
                                  JsonPlaceholderFallbackHandler jsonPlaceholderFallbackHandler,
                                  @Qualifier("taskExecutor") ExecutorService taskExecutor) {
        this.jsonPlaceholderClient = jsonPlaceholderClient;
        this.objectMapper = objectMapper;
        this.jsonPlaceholderFallbackHandler = jsonPlaceholderFallbackHandler;
        this.taskExecutor = taskExecutor;
    }

    @Override
    @CircuitBreaker(name = "jsonPlaceholderClient", fallbackMethod = "getUsersFallback")
    @Retry(name = "jsonPlaceholderClient")
    @TimeLimiter(name = "jsonPlaceholderClient")
    @Cacheable(value = "users", key = "'all'")
    public CompletableFuture<ApiResponse<List<JsonPlaceholderResponse>>> getUsers() {
        return CompletableFuture.supplyAsync(() -> {
            List<JsonPlaceholderResponse> usersData = jsonPlaceholderClient.getUsers();
            logger.debug("Provider response from get users: {}", objectMapper.writeValueAsString(usersData));
            return ApiResponse.success(usersData);
        }, taskExecutor);
    }

    @Override
    public ApiResponse<JsonPlaceholderResponse> createUser(JsonPlaceholderRequest request) {
        JsonPlaceholderResponse user = jsonPlaceholderClient.createUser(request);
        logger.debug("Provider response: {}", objectMapper.writeValueAsString(user));
        return ApiResponse.success("User created successfully", user);
    }

    private CompletableFuture<ApiResponse<List<JsonPlaceholderResponse>>> getUsersFallback(Throwable t) {
        return jsonPlaceholderFallbackHandler.handleGetUsersFailure(t);
    }

}
