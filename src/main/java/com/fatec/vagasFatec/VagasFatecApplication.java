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

	//Caso vier a dar problema de compilação por motivos não logicos como exmp: Adicionei uma coluna e não builda mais. Rodar um:
	// docker compose down -v
	// docker compose build --no-cache
	// docker compose up
}
