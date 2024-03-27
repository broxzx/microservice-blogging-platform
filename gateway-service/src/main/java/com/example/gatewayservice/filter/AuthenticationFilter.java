package com.example.gatewayservice.filter;

import com.example.gatewayservice.jwtUtils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtils utils;


    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("token is missing");
                }

                String header = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (header != null && header.startsWith("Bearer ")) {
                    header = header.substring(7);

                    try {
                        utils.validate(header);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return chain.filter(exchange);
        }));
    }

    public static class Config {

    }
}
