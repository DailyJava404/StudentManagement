package com.example.studentsmanagement.Model.Response;


import com.example.studentsmanagement.Enum.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {

    private String token;
    private String username;
    private Role role;

    public AuthResponse() {
    }

    public AuthResponse(String token, String username, Role role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

}