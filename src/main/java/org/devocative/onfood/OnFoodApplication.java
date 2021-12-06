package org.devocative.onfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

// TIP: for exclude, https://stackoverflow.com/questions/30761253/remove-using-default-security-password-on-spring-boot
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class OnFoodApplication {

	public static void main(String[] args) {
		// TIP: due to  warning in log saying 'liquibase.hub: Skipping auto-registration'
		System.setProperty("liquibase.hub.mode", "off");

		SpringApplication.run(OnFoodApplication.class, args);
	}

}
