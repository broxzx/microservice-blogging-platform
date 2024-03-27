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

    public Predicate<ServerHttpRequest> isSecured =
            request -> allowedRoutes
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
