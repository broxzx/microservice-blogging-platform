package com.example.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtUserRequest {
    private String username;

    private String password;

    private String email;
}
