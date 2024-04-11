package com.messageservice.messageservice.service;

import com.messageservice.messageservice.model.UserModelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient webClient;

    public UserModelResponse getUserModelById(String token) {
        String userToken;
        if (token != null && token.startsWith("Bearer ")) {
            userToken = token.substring(7);
        } else {
            userToken = null;
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
