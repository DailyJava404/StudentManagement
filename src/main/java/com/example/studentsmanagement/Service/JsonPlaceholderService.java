package com.example.studentsmanagement.Service;

import com.example.studentsmanagement.Client.JsonPlaceholderClient;
import com.example.studentsmanagement.Interface.IJsonPlaceholderService;
import com.example.studentsmanagement.Model.Request.JsonPlaceholderRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.JsonPlaceholderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class JsonPlaceholderService implements IJsonPlaceholderService {

    private final JsonPlaceholderClient jsonPlaceholderClient;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(JsonPlaceholderService.class);
    public JsonPlaceholderService(JsonPlaceholderClient jsonPlaceholderClient, ObjectMapper objectMapper) {
        this.jsonPlaceholderClient = jsonPlaceholderClient;
        this.objectMapper = objectMapper;
    }
    @Override
    @Cacheable(value = "users", key = "'all'")
    public ApiResponse<List<JsonPlaceholderResponse>> getUsers() {
        List<JsonPlaceholderResponse> usersData = jsonPlaceholderClient.getUsers();
        logger.debug("Provider response from get users: {}", objectMapper.writeValueAsString(usersData));
        return ApiResponse.success(usersData);
    }

    @Override
    public ApiResponse<JsonPlaceholderResponse> createUser(JsonPlaceholderRequest request) {
        JsonPlaceholderResponse user = jsonPlaceholderClient.createUser(request);
        logger.debug("Provider response: {}", objectMapper.writeValueAsString(user));
        return ApiResponse.success("User created successfully", user);
    }

}
