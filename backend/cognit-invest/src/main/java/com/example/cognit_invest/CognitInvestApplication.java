package com.example.cognit_invest;

import com.example.cognit_invest.services.internal.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CognitInvestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CognitInvestApplication.class, args);
		Logger.info("Servidor iniciado");
	}

}
