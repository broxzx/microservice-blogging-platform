package com.example.blogservice.service;

import com.example.blogservice.exception.TokenIsInvalidException;
import com.example.blogservice.model.UserModelResponse;
import com.example.blogservice.utils.JwtTokenUtils;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

/**
 * The UserService class provides methods for user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient webClient;

    private final JwtTokenUtils jwtTokenUtils;

    private final Tracer tracer;

    @Value("${host.name}")
    private String host;

    /**
     * Finds the user ID associated with the given JWT token.
     *
     * @param token The JWT token.
     * @return The user ID as a string.
     */
    public String findUserIdByJwtToken(String token) {
        return Objects.requireNonNull(findUserEntityByJwtToken(token).userId());
    }

    /**
     * Verifies the given JWT token.
     *
     * @param token The JWT token to be verified.
     * @return The verified token.
     * @throws TokenIsInvalidException If the token is invalid or absent.
     */
    public String verifyToken(String token) {
        return jwtTokenUtils.verifyToken(token);
    }

    /**
     * Finds the user entity for the given JWT token.
     *
     * @param token The JWT token.
     * @return The user entity wrapped in a UserModelResponse object.
     * @throws RuntimeException If an error occurs while retrieving the user entity.
     */
    public UserModelResponse findUserEntityByJwtToken(String token) {
        Span userEntityLookUp = tracer.nextSpan().name("User Entity Look up");

        try (Tracer.SpanInScope ignored = tracer.withSpan(userEntityLookUp.start())) {

            return webClient
                    .get()
                    .uri("http://%s:8080/user/by-jwt-token".formatted(host), uriBuilder -> uriBuilder
                            .queryParam("token", token)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .bodyToMono(UserModelResponse.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            userEntityLookUp.end();
        }
    }
}
