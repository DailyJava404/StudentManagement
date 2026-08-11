package com.example.studentsmanagement.Service;

import com.example.studentsmanagement.Client.UserClient;
import com.example.studentsmanagement.Interface.IJsonPlaceholder;
import com.example.studentsmanagement.Model.Request.JsonPlaceholderRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.JsonPlaceholderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonPlaceholderService implements IJsonPlaceholder {

    private final UserClient userClient;
    private static final Logger logger = LoggerFactory.getLogger(JsonPlaceholderService.class);
    public JsonPlaceholderService(UserClient userClient) {
        this.userClient = userClient;
    }
    @Override
    public ApiResponse<List<JsonPlaceholderResponse>> getUsers() {
        logger.info("Calling Provider to get users");
        List<JsonPlaceholderResponse> usersData = userClient.getUsers();
        logger.info("Provider response: {}", usersData);
        return ApiResponse.success(usersData);
    }

    @Override
    public ApiResponse<JsonPlaceholderResponse> createUser(JsonPlaceholderRequest request) {
        logger.info("Calling Provider to create users: {}", request);
        JsonPlaceholderResponse user = userClient.createUser(request);
        logger.info("Provider response: {}", user);
        return ApiResponse.success("User created successfully", user);
    }

}
