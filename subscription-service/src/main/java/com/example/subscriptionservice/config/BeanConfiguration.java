package com.example.subscriptionservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * This class is a Spring configuration class that defines beans for the application.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Creates and configures a WebClient instance.
     *
     * @return a configured WebClient instance.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }
}
