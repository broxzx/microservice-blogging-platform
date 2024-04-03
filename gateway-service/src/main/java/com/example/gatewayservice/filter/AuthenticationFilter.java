package com.example.gatewayservice.filter;

import com.example.gatewayservice.exception.TokenIsAbsentException;
import com.example.gatewayservice.exception.TokenIsNotValidException;
import com.example.gatewayservice.jwtUtils.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;

    private final JwtUtils jwtUtils;


    public AuthenticationFilter(RouteValidator routeValidator, JwtUtils jwtUtils) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            // all paths except "/security/registration", "/security/validate", "/security/login"
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new TokenIsAbsentException("token is missing");
                }

                String header =  exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (header != null && header.startsWith("Bearer ")) {
                    header = header.substring(7);

                    try {
                        jwtUtils.validate(header);
                    } catch (Exception e) {
                        throw new TokenIsNotValidException("token is invalid");
                    }
                }
            }

            // check authority for admin-method
            if (routeValidator.isAdminRequest.test(exchange.getRequest())) {
                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                } else {
                    throw new TokenIsNotValidException("token is invalid");
                }

                List<String> roles = jwtUtils.getJwtRoles(token);

                System.out.println(roles);

                if (!roles.contains("ROLE_ADMIN")) {
                    throw new RuntimeException("you don't have access to this page");
                }
            }

            return chain.filter(exchange);
        }));
    }

    public static class Config {

    }
}
