package com.example.security.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtUserResponse {

    private String username;

    private String password;
}
