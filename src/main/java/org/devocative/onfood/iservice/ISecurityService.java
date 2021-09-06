package org.devocative.onfood.iservice;

public interface ISecurityService {
	String RESTAURATEUR_ROLE = "Restaurateur";

	void authenticateByToken(String token);

	String generateToken(String username, Long userId, String role);
}
