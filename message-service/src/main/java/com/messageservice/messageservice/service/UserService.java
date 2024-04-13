package com.messageservice.messageservice.service;

import com.messageservice.messageservice.exception.TokenIsInvalidException;
import com.messageservice.messageservice.model.UserModelResponse;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

/**
 * The UserService class provides methods for retrieving user information.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient webClient;

    private final Tracer tracer;

    /**
     * Retrieves the user model by the provided token.
     *
     * @param token The authentication token.
     * @return The UserModelResponse representing the user model.
     * @throws TokenIsInvalidException if the token is absent or invalid.
     */
    public UserModelResponse getUserModelByToken(String token) {
        Span userEntityLookUp = tracer.nextSpan().name("User Entity LookUp");

        try (Tracer.SpanInScope ignored = tracer.withSpan(userEntityLookUp.start())) {
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
                    .subscribeOn(Schedulers.boundedElastic())
                    .doOnEach(signal -> {
                        if (signal.hasError()) {
                            Objects.requireNonNull(tracer.currentSpan()).tag("error", Objects.requireNonNull(signal.getThrowable()).getMessage());
                        }
                    })
                    .block();

            return userModelResponse;
        } finally {
            userEntityLookUp.end();
        }
    }
}
