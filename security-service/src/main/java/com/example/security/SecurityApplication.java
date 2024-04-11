package com.example.security;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.example"})
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {"com.example.security"})
public class SecurityApplication {

    public static void main(String... args) {
        new SpringApplicationBuilder(SecurityApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
