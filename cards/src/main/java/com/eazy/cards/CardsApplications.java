package com.eazy.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.eazy.cards.dto.CardsConfigDto;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = { CardsConfigDto.class })
public class CardsApplications {

	public static void main(String[] args) {
		SpringApplication.run(CardsApplications.class, args);
	}

}
