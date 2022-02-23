package org.devocative.onfood.iservice;

import org.springframework.security.core.context.SecurityContext;

public interface ISecurityService {
	String RESTAURATEUR_ROLE = "Restaurateur";

	void authenticateByToken(String token);

	void authenticateByToken(String token, SecurityContext context);

	String createToken(String username, Long userId, String role);
}
