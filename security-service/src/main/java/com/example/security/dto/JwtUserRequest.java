package com.example.security.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * A class representing a request to authenticate a user using JWT (JSON Web Token).
 */
@Data
@Builder
@ConditionalOnProperty(prefix = "security", name = "jwt.enabled", matchIfMissing = false)
public class JwtUserRequest {
    private String username;

    private String password;

    private String email;
}
