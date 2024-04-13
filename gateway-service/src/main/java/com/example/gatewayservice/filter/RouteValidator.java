package com.example.gatewayservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * The RouteValidator class is responsible for validating routes and checking if a request is considered secured or if it
 * corresponds to an admin request.
 */
@Component
public class RouteValidator {

    /**
     * The allowedRoutes variable is a private static final List of Strings. It contains a predefined list of routes that
     * are considered allowed for validation.
     * <p>
     * The routes included in the list are:
     * 1. /security/registration
     * 2. /security/validate
     * 3. /security/login
     * <p>
     * This variable is used in the RouteValidator class to validate routes and determine if a request is considered secured.
     */
    private static final List<String> allowedRoutes = List.of(
            "/security/registration",
            "/security/validate",
            "/security/login"
    );

    /**
     * The adminPaths variable is a private static final List of Strings. It contains a predefined list of paths that are considered
     * as admin paths. These paths are used to determine if a request is considered an admin request.
     * <p>
     * The paths included in the list are:
     * 1. /swagger-ui/
     * <p>
     * This variable is used in the RouteValidator class to identify admin requests.
     */
    private static final List<String> adminPaths = List.of(
            "/swagger-ui/"
    );

    /**
     * A predicate to check if a given request is considered secured.
     * <p>
     * This predicate is used to validate routes and determine if a request is considered secured based on the allowed routes
     * defined in the "allowedRoutes" variable in the {@link RouteValidator} class.
     */
    public Predicate<ServerHttpRequest> isSecured =
            request -> allowedRoutes
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    /**
     * A predicate that checks if the given request is an admin request.
     * An admin request is identified by matching the request URI path to the predefined admin paths.
     */
    public Predicate<ServerHttpRequest> isAdminRequest =
            request -> adminPaths
                    .stream()
                    .anyMatch(uri -> request.getURI().getPath().contains(uri));
}
