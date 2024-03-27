package com.example.security;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SecurityApplication {

    public static void main(String... args) {
        new SpringApplicationBuilder(SecurityApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
