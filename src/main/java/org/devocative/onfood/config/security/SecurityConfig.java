package org.devocative.onfood.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final SecurityTokenFilter tokenFilter;
	//private final SecurityTokenContextRepository repository;

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
				"/api/foods/searches",
				"/api/j4d/registrations/*",
				"/actuator/**",
				"/swagger-resources/**",
				"/swagger-ui/**",
				"/v3/api-docs/**"
			).permitAll()
			.anyRequest().authenticated()
			.and()
			// TIP: By default, HttpServletResponse.SC_FORBIDDEN is sent!
			//.exceptionHandling().authenticationEntryPoint((rq, rs, e) -> rs.sendError(HttpServletResponse.SC_UNAUTHORIZED))
			.exceptionHandling().authenticationEntryPoint((rq, rs, e) -> {
				log.warn("ExceptionHandling -> AuthenticationEntryPoint: {}", e.getMessage());

				rs.setContentType("application/json");
				rs.setCharacterEncoding("UTF-8");
				rs.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				final PrintWriter writer = rs.getWriter();
				writer.write("{\"code\": \"InvalidAuthentication\"}");
				writer.flush();
			})
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		// TIP: use `securityContextRepository` instead of `tokenFilter`, and vice versa
		//.and()
		//.securityContext().securityContextRepository(repository)
		;

		http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

	// [3]
	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	// ------------------------------

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
