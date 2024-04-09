package com.example.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDtoResponse {

    Long userId;

    private String username;

    private String email;
}
