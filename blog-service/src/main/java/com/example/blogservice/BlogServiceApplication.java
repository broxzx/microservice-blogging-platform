package com.example.blogservice;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BlogServiceApplication {

    public static void main(String... args) {
        new SpringApplicationBuilder(BlogServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
