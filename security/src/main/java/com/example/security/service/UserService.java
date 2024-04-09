package com.example.security.service;

import com.example.security.dto.UserDtoResponse;
import com.example.security.entity.UserEntity;
import com.example.security.exception.UserNotFoundException;
import com.example.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Transactional
    public UserEntity saveUser(UserEntity user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public UserDtoResponse getUserResponseById(Long id) {
        UserEntity foundUserById = userRepository.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException("user with id '%d' was not found".formatted(id))
                );

        return UserDtoResponse.builder()
                .userId(foundUserById.getId())
                .username(foundUserById.getUsername())
                .email(foundUserById.getEmail())
                .build();
    }


}
