package com.example.security.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a response containing user information for JWT authentication.
 */
@Builder
@Data
public class JwtUserResponse {

    private String username;

    private String password;

    private String email;
}
