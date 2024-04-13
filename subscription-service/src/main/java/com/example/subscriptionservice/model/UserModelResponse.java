package com.example.subscriptionservice.model;

import com.example.subscriptionservice.entity.Role;

/**
 * The UserModelResponse class represents a response model for a user.
 */
public record UserModelResponse(Long userId, String username, String email, Role role) {
}
