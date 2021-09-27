package org.devocative.onfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class OnFoodApplication {

	@Bean
	public AuditorAware<String> auditorAware() {
		//TODO - temporal, waiting for security
		return () -> Optional.of("anonymous");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// ------------------------------

	public static void main(String[] args) {
		SpringApplication.run(OnFoodApplication.class, args);
	}

}
