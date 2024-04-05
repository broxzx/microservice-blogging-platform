package com.example.subscriptionservice.service;

import com.example.entityservice.entity.UserEntity;
import com.example.subscriptionservice.exception.NotFoundException;
import com.example.subscriptionservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException("user with id '%d' was not found".formatted(userId))
                );
    }
}
