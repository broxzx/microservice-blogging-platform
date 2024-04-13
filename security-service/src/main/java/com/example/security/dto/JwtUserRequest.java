package com.example.security.dto;

import lombok.Builder;
import lombok.Data;

/**
 * A class representing a request to authenticate a user using JWT (JSON Web Token).
 */
@Data
@Builder
public class JwtUserRequest {
    private String username;

    private String password;

    private String email;
}
