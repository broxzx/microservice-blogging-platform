package com.example.security.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * BeanConfiguration class is responsible for configuring and providing various beans used in the application.
 */
@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

//    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Configures the security filter chain for the application.
     *
     * @param http the {@link HttpSecurity} to use for configuring the filter chain
     * @return the {@link SecurityFilterChain} configured with the specified rules
     * @throws Exception if an error occurs while configuring the filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/security/login", "/security/registration", "security/validate", "/user/**").permitAll()
                        .requestMatchers("/swagger-ui/index.html").hasRole("ADMIN"))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(((request, response, accessDeniedException) -> response.sendRedirect("/register"))))
                .logout(LogoutConfigurer::permitAll)
                .build();
    }

    /**
     * Returns a new instance of BCryptPasswordEncoder for password encoding.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    @ConditionalOnProperty(prefix = "security", name = "jwt.enabled", matchIfMissing = false)
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Retrieves the authentication manager from the given AuthenticationConfiguration.
     *
     * @param authenticationConfiguration the AuthenticationConfiguration object
     * @return the AuthenticationManager retrieved from the authenticationConfiguration
     * @throws Throwable if an exception is thrown while retrieving the authentication manager
     */
    @Bean
    @SneakyThrows
    @ConditionalOnProperty(prefix = "security", name = "jwt.enabled", matchIfMissing = false)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Creates and configures an instance of {@link DaoAuthenticationProvider} for authentication purposes.
     *
     * @return the configured {@link DaoAuthenticationProvider} instance
     */
    @Bean
    @ConditionalOnProperty(prefix = "security", name = "jwt.enabled", matchIfMissing = false)
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(encoder());
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }
}
