package com.example.blogservice.service;

import com.example.blogservice.model.UserModelResponse;
import com.example.blogservice.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient webClient;

    private final JwtTokenUtils jwtTokenUtils;

    public String findUserIdByJwtToken(String token) {
        return String.valueOf(Objects.requireNonNull(findUserEntityByJwtToken(token).userId()));
    }

    public String verifyToken(String token) {
        return jwtTokenUtils.verifyToken(token);
    }

    public UserModelResponse findUserEntityByJwtToken(String token) {
        String username = jwtTokenUtils.getUsername(token);
        UserModelResponse userModelResponse = webClient
                .get()
                .uri("http://localhost:8080/user/by-username", uriBuilder -> uriBuilder
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .bodyToMono(UserModelResponse.class)
                .block();

        return userModelResponse;
    }
}
