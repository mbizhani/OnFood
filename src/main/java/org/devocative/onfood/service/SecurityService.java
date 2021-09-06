package org.devocative.onfood.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.iservice.ISecurityService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SecurityService implements ISecurityService {
	private static final String USER_ID_CLAIM = "uid";
	private static final String ROLE_CLAIM = "role";

	private final String key = "qazWSX@123";

	// ------------------------------

	@Override
	public void authenticateByToken(String token) {
		final Claims claims = Jwts.parser()
			.setSigningKey(key)
			.parseClaimsJws(token)
			.getBody();

		final Date expirationDate = assertValue(claims.getExpiration(), "Invalid Token: No Expiration Claim");
		if (expirationDate.before(new Date())) {
			throw new BadCredentialsException("Expired Token: " + claims.getExpiration());
		}

		final Long userId = assertValue(claims.get(USER_ID_CLAIM, Long.class), "Invalid Token: No UID");
		final String role = assertValue(claims.get(ROLE_CLAIM, String.class), "Invalid Token: No Role");
		final String subject = assertValue(claims.getSubject(), "Invalid Token: No Subject");

		log.debug("AuthenticateByToken: userId=[{}] subject=[{}] role=[{}]", userId, subject, role);

		final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
			subject,
			userId,
			Collections.singletonList(new SimpleGrantedAuthority(role)));
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

	@Override
	public String generateToken(String username, Long userId, String role) {
		final Map<String, Object> claims = new HashMap<>();
		claims.put(USER_ID_CLAIM, userId);
		claims.put(ROLE_CLAIM, role);

		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, 1);

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(cal.getTime())
			.signWith(SignatureAlgorithm.HS512, key)
			.compact();
	}

	// ------------------------------

	private <T> T assertValue(T val, String message) {
		if (val == null) {
			throw new BadCredentialsException(message);
		}
		return val;
	}
}
