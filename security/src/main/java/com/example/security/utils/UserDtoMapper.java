package com.example.security.utils;

import com.example.security.dto.UserDtoResponse;
import com.example.security.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public UserDtoResponse makeUserDtoResponse(UserEntity user) {
        return UserDtoResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
