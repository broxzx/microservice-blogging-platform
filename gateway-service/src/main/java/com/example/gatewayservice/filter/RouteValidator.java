package com.example.gatewayservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final List<String> allowedRoutes = List.of(
            "/security/registration",
            "/security/validate",
            "/security/login"
    );

    private static final List<String> adminPaths = List.of(
            "/swagger-ui/"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> allowedRoutes
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    public Predicate<ServerHttpRequest> isAdminRequest =
            request -> adminPaths
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().contains(uri));
}
