package com.example.security.controller;

import com.example.security.dto.JwtUserRequest;
import com.example.security.dto.JwtUserResponse;
import com.example.security.entity.UserEntity;
import com.example.security.security.UserDetailsServiceImpl;
import com.example.security.service.UserService;
import com.example.security.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller class responsible for handling security-related requests such as login and registration.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("security")
public class Controller {

    private final UserService service;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    private static final String LOGIN = "/login";

    private static final String REGISTRATION = "/registration";

    /**
     * Authenticates the user and generates a JWT token.
     *
     * @param user The credentials of the user trying to login
     * @return The generated JWT token
     * @throws RuntimeException If the password is incorrect
     */
    @PostMapping(LOGIN)
    public String login(@RequestBody JwtUserRequest user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null));
        } catch (BadCredentialsException e) {
            throw new RuntimeException("wrong password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtTokenProvider.getToken(userDetails);

        return token;
    }

    /**
     * Registers a new user.
     *
     * @param jwtUserRequest The user information for registration
     * @return The response entity containing the registered user's information
     */
    @PostMapping(REGISTRATION)
    public ResponseEntity<JwtUserResponse> registration(@RequestBody JwtUserRequest jwtUserRequest) {
        service.saveUser(UserEntity.builder()
                .username(jwtUserRequest.getUsername())
                .password(jwtUserRequest.getPassword())
                .email(jwtUserRequest.getEmail())
                .build());

        JwtUserResponse jwtUserResponse = JwtUserResponse.builder()
                .username(jwtUserRequest.getUsername())
                .password(jwtUserRequest.getPassword())
                .email(jwtUserRequest.getEmail())
                .build();

        return ResponseEntity.ok(jwtUserResponse);
    }

}
