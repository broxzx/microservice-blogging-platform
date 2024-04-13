package com.messageservice.messageservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * This class provides the configuration for creating the beans used in the application.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Creates and configures a WebClient instance.
     *
     * @return The configured WebClient instance.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }
}
