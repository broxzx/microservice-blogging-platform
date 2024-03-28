package com.example.configservice;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServiceApplication {

	public static void main(String... args) {
		new SpringApplicationBuilder(ConfigServiceApplication.class)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
	}

}
