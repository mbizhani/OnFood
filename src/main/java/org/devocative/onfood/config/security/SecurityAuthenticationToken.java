package org.devocative.onfood.config.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class SecurityAuthenticationToken extends AbstractAuthenticationToken {
	private final String username;
	private final Long userId;

	public SecurityAuthenticationToken(String username, Long userId, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.username = username;
		this.userId = userId;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		throw new RuntimeException("JwtAuthenticationToken: No Credentials");
	}

	@Override
	public Object getPrincipal() {
		return userId;
	}
}
