package com.example.gatewayservice;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApplication {

    public static void main(String... args) {
        new SpringApplicationBuilder(GatewayServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
