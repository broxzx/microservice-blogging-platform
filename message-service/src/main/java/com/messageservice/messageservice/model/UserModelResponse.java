package com.messageservice.messageservice.model;

import com.messageservice.messageservice.utils.Role;

/**
 * Represents a response model for user information.
 */
public record UserModelResponse(Long userId, String username, String email, Role role) {
}
