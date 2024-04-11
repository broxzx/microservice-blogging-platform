package com.messageservice.messageservice;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MessageServiceApplication {

    public static void main(String... args) {
        new SpringApplicationBuilder(MessageServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
