package com.example.security.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Represents a response containing user information for JWT authentication.
 */
@Builder
@Data
@ConditionalOnProperty(prefix = "security", name = "jwt.enabled", matchIfMissing = false)
public class JwtUserResponse {

    private String username;

    private String password;

    private String email;
}
