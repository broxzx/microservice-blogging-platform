package com.example.blogservice.model;

import com.example.blogservice.entity.Role;

/**
 * Represents the response model for a user.
 */
public record UserModelResponse(Long userId, String username, String email, Role role) {
}
