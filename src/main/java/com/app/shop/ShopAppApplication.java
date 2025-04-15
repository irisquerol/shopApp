package com.app.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class ShopAppApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(ShopAppApplication.class, args);
	}

}
