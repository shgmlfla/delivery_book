package com.example.dbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbookApplication.class, args);
	}

}
