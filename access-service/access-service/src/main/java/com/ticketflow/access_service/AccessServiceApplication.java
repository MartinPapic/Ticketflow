package com.ticketflow.access_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccessServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessServiceApplication.class, args);
	}

}
