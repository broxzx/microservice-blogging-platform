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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("security")
public class Controller {

    private final UserService service;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
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

    @PostMapping("/registration")
    public ResponseEntity<JwtUserResponse> registration(@RequestBody JwtUserRequest jwtUserRequest) {
        UserEntity savedUser = service.saveUser(UserEntity.builder()
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

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        jwtTokenProvider.validate(token);

        return ResponseEntity.ok("token is valid!");
    }
}
