package com.example.blogservice;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"com.example"})
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = {"com.example.blogservice"})
public class BlogServiceApplication {

    public static void main(String... args) {
        new SpringApplicationBuilder(BlogServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
