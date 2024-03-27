package com.example.gatewayservice.filter;

import com.example.gatewayservice.exception.TokenIsAbsent;
import com.example.gatewayservice.exception.TokenIsNotValid;
import com.example.gatewayservice.jwtUtils.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;

    private final JwtUtils utils;


    public AuthenticationFilter(RouteValidator routeValidator, JwtUtils utils) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.utils = utils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new TokenIsAbsent("token is missing");
                }

                String header =  exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (header != null && header.startsWith("Bearer ")) {
                    header = header.substring(7);

                    try {
                        utils.validate(header);
                    } catch (Exception e) {
                        throw new TokenIsNotValid("token is invalid");
                    }
                }
            }

            return chain.filter(exchange);
        }));
    }

    public static class Config {

    }
}
