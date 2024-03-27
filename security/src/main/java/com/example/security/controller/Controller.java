package com.example.security.controller;

import com.example.security.dto.JwtRequest;
import com.example.security.entity.UserEntity;
import com.example.security.security.UserDetailsServiceImpl;
import com.example.security.service.UserService;
import com.example.security.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/app")
    public String greetings() {
        return "hello from authenticated method!";
    }


    @PostMapping("/login")
    public String login(@RequestBody JwtRequest user) {
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
    public void registration(@RequestBody JwtRequest jwtRequest) {
        service.saveUser(UserEntity.builder()
                .username(jwtRequest.getUsername())
                .password(jwtRequest.getPassword())
                .build());
//        service.saveUser(new UserEntity(null, jwtRequest.getUsername(), jwtRequest.getPassword(), "user"));
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam String token) {
        jwtTokenProvider.validate(token);
        return "token is valid";
    }
}
