package com.messageservice.messageservice.service;

import com.messageservice.messageservice.exception.TokenIsInvalidException;
import com.messageservice.messageservice.model.UserModelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient webClient;

    public UserModelResponse getUserModelByToken(String token) {
        String userToken;
        if (token != null && token.startsWith("Bearer ")) {
            userToken = token.substring(7);
        } else {
            throw new TokenIsInvalidException("token is either absent or invalid. please login by using 'http://localhost:8080/security/login'");
        }

        UserModelResponse userModelResponse = webClient
                .get()
                .uri("http://localhost:8080/user/by-jwt-token", uriBuilder -> uriBuilder
                        .queryParam("token", userToken)
                        .build())
                .retrieve()
                .bodyToMono(UserModelResponse.class)
                .block();

        return userModelResponse;
    }
}
