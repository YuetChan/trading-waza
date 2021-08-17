package com.tycorp.eb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.tycorp.eb.*")
@ComponentScan(basePackages = "com.tycorp.eb.*")
public class EveningBrewPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(EveningBrewPortalApplication.class, args);
	}

}
