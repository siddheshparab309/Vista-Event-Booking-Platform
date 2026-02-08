package com.events.superapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class EventBookingSuperappApplication {
	public static void main(String[] args) {
		SpringApplication.run(EventBookingSuperappApplication.class, args);
	}
}
