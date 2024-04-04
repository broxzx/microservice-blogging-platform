package com.example.subscriptionservice;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.example")
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = "com.example.subscriptionservice")
public class SubscriptionServiceApplication {

    public static void main(String... args) {
        new SpringApplicationBuilder(SubscriptionServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
