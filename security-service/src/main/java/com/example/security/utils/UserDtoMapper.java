package com.example.security.utils;

import com.example.security.dto.UserDtoResponse;
import com.example.security.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * The UserDtoMapper class is responsible for mapping UserEntity objects to UserDtoResponse objects.
 */
@Component
public class UserDtoMapper {

    /**
     * Creates a UserDtoResponse object based on the provided UserEntity object.
     *
     * @param user The UserEntity object to be converted.
     * @return The UserDtoResponse object representing the user.
     */
    public UserDtoResponse makeUserDtoResponse(UserEntity user) {
        return UserDtoResponse.builder()
                .userId(String.valueOf(user.getId()))
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
