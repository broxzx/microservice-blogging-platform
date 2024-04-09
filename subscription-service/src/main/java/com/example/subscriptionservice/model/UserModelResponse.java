package com.example.subscriptionservice.model;

import com.example.subscriptionservice.entity.Role;

public record UserModelResponse(Long userId, String username, String email, Role role) {
}
