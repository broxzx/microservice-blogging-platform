package com.example.eurekadiscoveryserver;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaDiscoveryServerApplication {

    public static void main(String... args) {
        new SpringApplicationBuilder(EurekaDiscoveryServerApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
