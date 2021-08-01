package org.devocative.onfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class OnFoodApplication {

	@Bean
	public AuditorAware<String> auditorAware() {
		//TODO - temporal, waiting for security
		return () -> Optional.of("anonymous");
	}

	public static void main(String[] args) {
		SpringApplication.run(OnFoodApplication.class, args);
	}

}
