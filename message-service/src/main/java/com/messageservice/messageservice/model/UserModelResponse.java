package com.messageservice.messageservice.model;

import com.messageservice.messageservice.utils.Role;

public record UserModelResponse(Long userId, String username, String email, Role role) {
}
