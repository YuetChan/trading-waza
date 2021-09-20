package com.tycorp.tb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.tycorp.tb.*")
@ComponentScan(basePackages = "com.tycorp.tb.*")
public class TradingBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingBoardApplication.class, args);
	}

}
