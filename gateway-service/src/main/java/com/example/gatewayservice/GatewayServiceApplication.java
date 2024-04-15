package com.example.gatewayservice;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@Log4j2
@RestController
public class GatewayServiceApplication {

    public static void main(String... args) {
        new SpringApplicationBuilder(GatewayServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @GetMapping("/api/v1")
    public String getApiVersion(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        log.info(oAuth2AuthenticationToken);
        return "authorized user!";
    }
}
