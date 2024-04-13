package com.example.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for defining beans in the application context.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Creates a new instance of RestTemplate.
     * This method is used to define a bean in the application context.
     *
     * @return the RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
