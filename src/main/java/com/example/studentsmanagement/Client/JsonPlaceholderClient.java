package com.example.studentsmanagement.Client;


import com.example.studentsmanagement.Model.Request.JsonPlaceholderRequest;
import com.example.studentsmanagement.Model.Response.JsonPlaceholderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "jsonPlaceholderClient",
        url = "${jsonplaceholder.baseUrl}"
)
public interface JsonPlaceholderClient {
    @GetMapping("/users")
    List<JsonPlaceholderResponse> getUsers();

    @PostMapping("/users")
    JsonPlaceholderResponse createUser(@RequestBody JsonPlaceholderRequest request);
}
