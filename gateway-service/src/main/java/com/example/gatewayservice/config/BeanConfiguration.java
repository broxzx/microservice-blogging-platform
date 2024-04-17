package com.example.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

/**
 * Configuration class for defining beans in the application context.
 */
@Configuration
@EnableWebFluxSecurity
public class BeanConfiguration {

    /**
     * Configures the security web filter chain for the application.
     *
     * @param http The {@link ServerHttpSecurity} instance to configure the security settings.
     * @return The configured {@link SecurityWebFilterChain}.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchange -> authorizeExchange
                        .pathMatchers("/swagger-ui/**").hasRole("ADMIN")
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()))
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    /**
     * Returns the OAuth2UserService for handling the OIDC user request.
     *
     * @return The OAuth2UserService<OidcUserRequest, OidcUser> instance.
     */
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserRequest() {
        OidcUserService oidcUserService = new OidcUserService();

        return userRequest -> {
            OidcUser oidcUser = oidcUserService.loadUser(userRequest);

            List<String> groups = oidcUser.getClaimAsStringList("groups");

            List<SimpleGrantedAuthority> authorities = groups
                    .stream()
                    .filter(authority -> authority.startsWith("ROLE_"))
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }

}
