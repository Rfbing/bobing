package com.piggysoft.bobing;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BobinggamblingApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BobinggamblingApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		//SpringApplication.run(BobinggamblingApplication.class, args)
	}
}
