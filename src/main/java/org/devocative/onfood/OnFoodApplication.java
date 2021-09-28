package org.devocative.onfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
// TIP: for exclude, https://stackoverflow.com/questions/30761253/remove-using-default-security-password-on-spring-boot
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
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
