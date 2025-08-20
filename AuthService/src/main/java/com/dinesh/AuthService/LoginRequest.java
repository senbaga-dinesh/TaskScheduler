package com.dinesh.AuthService;

import lombok.*;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
