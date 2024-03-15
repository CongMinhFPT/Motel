package com.motel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotelApplication.class, args);
	}

}
