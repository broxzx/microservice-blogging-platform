package com.example.entityservice;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EntityServiceApplication {

    public static void main(String... args) {
        new SpringApplicationBuilder(EntityServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
