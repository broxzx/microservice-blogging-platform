package com.example.gatewayservice.filter;

import com.example.gatewayservice.exception.AccessDeniedException;
import com.example.gatewayservice.exception.TokenIsAbsentException;
import com.example.gatewayservice.exception.TokenIsNotValidException;
import com.example.gatewayservice.jwtUtils.JwtUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Objects;

/**
 * This class represents an authentication filter for a gateway.
 * It provides functionality to validate and authorize incoming requests based on JWT authentication.
 * Requests to certain paths are considered secured and require a valid JWT token, while others are accessible without authentication.
 * Additionally, this filter checks if the user has the necessary authority to access admin-specific paths.
 */
@Component
@Log4j2
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;

    private final JwtUtils jwtUtils;


    public AuthenticationFilter(RouteValidator routeValidator, JwtUtils jwtUtils) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Applies the AuthenticationFilter to incoming requests in a gateway.
     * Validates and authorizes requests based on JWT authentication.
     * Secured routes require a valid JWT token, while admin-specific routes require the user to have ROLE_ADMIN authority.
     *
     * @param config the configuration for the filter (unused)
     * @return the GatewayFilter that validates and authorizes requests
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            // all paths except "/security/registration", "/security/validate", "/security/login"
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                String token = getAndVerifyJwtToken(exchange);

                try {
                    jwtUtils.validate(token);
                } catch (Exception e) {
                    throw new TokenIsNotValidException("token is invalid");
                }
            }

            // check authority for admin-method
            if (routeValidator.isAdminRequest.test(exchange.getRequest())) {
                String token = getAndVerifyJwtToken(exchange);

                List<String> roles = jwtUtils.getJwtRoles(token);

                if (!roles.contains("ROLE_ADMIN")) {
                    throw new AccessDeniedException("you don't have access to this page");
                }
            }

            return chain.filter(exchange);
        }));
    }

    /**
     * Retrieves and verifies a JWT token from the ServerWebExchange.
     *
     * @param exchange the current ServerWebExchange
     * @return the verified JWT token
     * @throws TokenIsAbsentException if the token is missing in the Authorization header
     * @throws TokenIsNotValidException if the token is not in the valid format or is invalid
     */
    private String getAndVerifyJwtToken(ServerWebExchange exchange) {
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new TokenIsAbsentException("token is missing");
        }

        String token = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            throw new TokenIsNotValidException("token is invalid");
        }

        return token;
    }

    /**
     * Represents a configuration class
     */
    public static class Config {

    }
}
