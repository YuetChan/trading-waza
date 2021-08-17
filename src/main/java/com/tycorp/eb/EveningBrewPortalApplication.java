package com.tycorp.eb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import java.time.Instant;

@SpringBootApplication
@EntityScan(basePackages = "com.tycorp.eb.*")
@ComponentScan(basePackages = "com.tycorp.eb.*")
public class EbApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbApplication.class, args);
	}

}
