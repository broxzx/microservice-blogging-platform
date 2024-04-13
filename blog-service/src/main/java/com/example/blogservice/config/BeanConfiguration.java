package com.example.blogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * This class is a configuration class that defines beans for the application.
 * It contains the necessary methods to configure and create beans.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Creates and configures a WebClient instance.
     *
     * @return the configured WebClient instance
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }
}
