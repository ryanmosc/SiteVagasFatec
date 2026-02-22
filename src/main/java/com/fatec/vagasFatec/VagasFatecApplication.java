package com.fatec.vagasFatec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VagasFatecApplication {

	public static void main(String[] args) {
		SpringApplication.run(VagasFatecApplication.class, args);
	}

}
