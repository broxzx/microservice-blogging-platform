package com.example.security.dto;

import com.example.security.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDtoResponse {

    private Long userId;

    private String username;

    private String email;

    private Role role;
}
