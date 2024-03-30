package com.example.blogservice.service;

import com.example.blogservice.exception.UserNotFoundException;
import com.example.blogservice.repository.UserRepository;
import com.example.blogservice.utils.JwtTokenUtils;
import com.example.entityservice.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtTokenUtils jwtTokenUtils;

    public String findUserIdByJwtToken(String token) {
        String username = jwtTokenUtils.getUsername(token);
        UserEntity foundUser = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("user with username '%s' was not found".formatted(username))
                );

        return String.valueOf(foundUser.getId());
    }

    public String verifyToken(String token) {
        return jwtTokenUtils.verifyToken(token);
    }

    public UserEntity findUserEntityByJwtToken(String token) {
        String username = jwtTokenUtils.getUsername(token);

        UserEntity foundUserEntity = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("user with username '%s' was not found".formatted(username))
                );

        return foundUserEntity;
    }
}
