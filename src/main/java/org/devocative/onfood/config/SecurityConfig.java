package org.devocative.onfood.config;

import lombok.RequiredArgsConstructor;
import org.devocative.onfood.model.AuditedUser;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final AuditedUser ANONYMOUS = new AuditedUser(-1L, "anonymous");

	private final JwtFilter jwtFilter;

	// ------------------------------

	// [1]
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
	}

	// [2]
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests().antMatchers(
				"/api/restaurateurs/registrations",
				"/api/restaurateurs/registrations/*",
				"/api/restaurateurs/logins",
				"/api/j4d/registrations/*",
				"/actuator/**").permitAll()
			.anyRequest().authenticated()
			.and()
			// TIP: By default, HttpServletResponse.SC_FORBIDDEN is sent!
			.exceptionHandling().authenticationEntryPoint((rq, rs, e) -> rs.sendError(HttpServletResponse.SC_UNAUTHORIZED))
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

	// [3]
	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	// ------------------------------

	@Bean
	public AuditorAware<AuditedUser> auditorAware() {
		//TIP: AnonymousAuthenticationToken [Principal=anonymousUser, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=127.0.0.1, SessionId=null], Granted Authorities=[ROLE_ANONYMOUS]]

		return () -> {
			final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth instanceof JwtAuthenticationToken) {
				JwtAuthenticationToken jwt = (JwtAuthenticationToken) auth;
				return Optional.of(new AuditedUser(jwt.getUserId(), jwt.getUsername()));
			}
			return Optional.of(ANONYMOUS);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
