package com.dinesh.AuthService;

import lombok.*;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role;
}