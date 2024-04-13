package com.example.blogservice.service;

import com.example.blogservice.model.UserModelResponse;
import com.example.blogservice.utils.JwtTokenUtils;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient webClient;

    private final JwtTokenUtils jwtTokenUtils;

    private final Tracer tracer;

    public String findUserIdByJwtToken(String token) {
        return String.valueOf(Objects.requireNonNull(findUserEntityByJwtToken(token).userId()));
    }

    public String verifyToken(String token) {
        return jwtTokenUtils.verifyToken(token);
    }

    public UserModelResponse findUserEntityByJwtToken(String token) {
        Span userEntityLookUp = tracer.nextSpan().name("User Entity Look up");

        try (Tracer.SpanInScope spanInScope = tracer.withSpan(userEntityLookUp.start())) {
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
        } finally {
            userEntityLookUp.end();
        }
    }
}
