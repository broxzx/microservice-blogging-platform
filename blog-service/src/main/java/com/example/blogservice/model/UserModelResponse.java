package com.example.blogservice.model;

import com.example.blogservice.entity.Role;

public record UserModelResponse(Long userId, String username, String email, Role role) {
}
