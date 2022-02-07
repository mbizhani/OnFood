package org.devocative.onfood.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.config.JwtAuthenticationToken;
import org.devocative.onfood.iservice.ISecurityService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SecurityService implements ISecurityService {
	private static final String USER_ID_CLAIM = "uid";
	private static final String ROLE_CLAIM = "role";

	private final String key = "qazWSX@123";

	// ------------------------------

	@Override
	public void authenticateByToken(String token) {
		final Claims claims;
		try {
			claims = Jwts.parser()
				.setSigningKey(key)
				.parseClaimsJws(token)
				.getBody();
		} catch (Exception e) {
			log.warn("SecurityService.authenticateByToken - JWT Parse: ({}) {}",
				e.getClass().getSimpleName(), e.getMessage());
			return;
		}

		final Long userId = assertValue(claims.get(USER_ID_CLAIM, Long.class), "Invalid Token: No UID");
		final String role = assertValue(claims.get(ROLE_CLAIM, String.class), "Invalid Token: No Role");
		final String subject = assertValue(claims.getSubject(), "Invalid Token: No Subject");

		log.debug("AuthenticateByToken: userId=[{}] subject=[{}] role=[{}]", userId, subject, role);

		setSecurityContext(subject, userId, role);
	}

	@Override
	public String generateToken(String username, Long userId, String role) {
		final Map<String, Object> claims = new HashMap<>();
		claims.put(USER_ID_CLAIM, userId);
		claims.put(ROLE_CLAIM, role);

		final Date expiration = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

		final String jwtToken = Jwts.builder()
			.setClaims(claims)
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(expiration)
			.signWith(SignatureAlgorithm.HS512, key)
			.compact();

		setSecurityContext(username, userId, role);

		return jwtToken;
	}

	// ------------------------------

	private <T> T assertValue(T val, String message) {
		if (val == null) {
			throw new BadCredentialsException(message);
		}
		return val;
	}

	private void setSecurityContext(String username, Long userId, String role) {
		final JwtAuthenticationToken authToken = new JwtAuthenticationToken(
			username,
			userId,
			Collections.singletonList(new SimpleGrantedAuthority(role)));
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
}
