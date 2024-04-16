package com.example.security.service;

import com.example.security.dto.UserDtoResponse;
import com.example.security.entity.UserEntity;
import com.example.security.exception.UserNotFoundException;
import com.example.security.repository.UserRepository;
import com.example.security.utils.UserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The UserService class is responsible for managing user-related operations such as user creation and retrieval.
 * It interacts with the UserRepository to store and fetch user data in the database.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

//    private final BCryptPasswordEncoder encoder;

//    private final JwtTokenProvider jwtTokenProvider;

    private final UserDtoMapper userDtoMapper;

    /**
     * Saves a user entity to the database.
     *
     * @param user the user entity to be saved
     */
    @Transactional
    public void saveUser(UserEntity user) {
//        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Retrieves a UserDtoResponse by the specified user ID.
     *
     * @param id The ID of the user.
     * @return The UserDtoResponse object corresponding to the specified user ID.
     * @throws UserNotFoundException If a user with the specified ID does not exist.
     */
    @Transactional(readOnly = true)
    public UserDtoResponse getUserResponseById(String id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException("user with id '%d' was not found".formatted(id))
                );

        return userDtoMapper.makeUserDtoResponse(userEntity);
    }

    /**
     * Retrieves a UserDtoResponse by the specified username.
     *
     * @param username The username of the user.
     * @return The UserDtoResponse object corresponding to the specified username.
     * @throws UserNotFoundException If a user with the specified username does not exist.
     */
    @Transactional(readOnly = true)
    public UserDtoResponse getUserResponseByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("user with username '%s' was not found".formatted(username))
                );

        return userDtoMapper.makeUserDtoResponse(userEntity);
    }

    /**
     * Retrieves a UserDtoResponse by the provided JWT token.
     *
     * @param token The JWT token provided as a string.
     * @return The UserDtoResponse object retrieved based on the token.
     * @throws UserNotFoundException If a user with the corresponding username is not found.
     */
    @Transactional
    @ConditionalOnProperty(prefix = "security", name = "jwt.enabled", matchIfMissing = false)
    public UserDtoResponse getUserDtoResponseByJwtToken(String token) {
//        String username = jwtTokenProvider.getUsername(token);

//        UserEntity userEntity = userRepository.findByUsername(username)
//                .orElseThrow(
//                        () -> new UserNotFoundException("user with username '%s' was not found".formatted(username))
//                );

        return userDtoMapper.makeUserDtoResponse(null);
    }

    /**
     * Finds a user entity in the database by email.
     *
     * @param email The email of the user.
     * @return The UserEntity object corresponding to the specified email.
     * @throws UserNotFoundException If a user with the specified email is not found.
     */
    @Transactional(readOnly = true)
    public UserDtoResponse findUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user with email '%s' was not found".formatted(email)));

        return userDtoMapper.makeUserDtoResponse(userEntity);
    }
}
