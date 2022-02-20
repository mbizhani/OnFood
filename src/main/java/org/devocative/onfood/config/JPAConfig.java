package org.devocative.onfood.config;

import org.devocative.onfood.config.security.SecurityAuthenticationToken;
import org.devocative.onfood.model.AuditedUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JPAConfig {
	private static final AuditedUser ANONYMOUS = new AuditedUser(-1L, "anonymous");

	@Bean
	public AuditorAware<AuditedUser> auditorAware() {
		//TIP: AnonymousAuthenticationToken [Principal=anonymousUser, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=127.0.0.1, SessionId=null], Granted Authorities=[ROLE_ANONYMOUS]]

		return () -> {
			final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth instanceof SecurityAuthenticationToken) {
				SecurityAuthenticationToken jwt = (SecurityAuthenticationToken) auth;
				return Optional.of(new AuditedUser(jwt.getUserId(), jwt.getUsername()));
			}
			return Optional.of(ANONYMOUS);
		};
	}
}
