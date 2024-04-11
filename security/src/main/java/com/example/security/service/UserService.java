package com.example.security.service;

import com.example.security.dto.UserDtoResponse;
import com.example.security.entity.UserEntity;
import com.example.security.exception.UserNotFoundException;
import com.example.security.repository.UserRepository;
import com.example.security.utils.JwtTokenProvider;
import com.example.security.utils.UserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDtoMapper userDtoMapper;

    @Transactional
    public UserEntity saveUser(UserEntity user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public UserDtoResponse getUserResponseById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException("user with id '%d' was not found".formatted(id))
                );

        return userDtoMapper.makeUserDtoResponse(userEntity);
    }

    @Transactional
    public UserDtoResponse getUserResponseByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("user with username '%s' was not found".formatted(username))
                );

        return userDtoMapper.makeUserDtoResponse(userEntity);
    }

    @Transactional
    public UserDtoResponse getUserDtoResponseByJwtToken(String token) {
        String username = jwtTokenProvider.getUsername(token);

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("user with username '%s' was not found".formatted(username))
                );

        return userDtoMapper.makeUserDtoResponse(userEntity);
    }

}
