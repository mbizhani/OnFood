package org.devocative.onfood.config.security;

import lombok.RequiredArgsConstructor;
import org.devocative.onfood.OnFoodProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class SecurityHandler {
	private final OnFoodProperties properties;

	public String readToken(HttpServletRequest request) {
		final String tokenKey = properties.getSecurity().getTokenKey();
		final String receivedTokenValue;

		switch (properties.getSecurity().getTokenMedia()) {
			case header:
				receivedTokenValue = request.getHeader(tokenKey);
				break;
			case cookie:
				receivedTokenValue = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
					.filter(c -> tokenKey.equals(c.getName()))
					.map(Cookie::getValue)
					.findFirst()
					.orElse(null);
				break;
			default:
				receivedTokenValue = null;
		}

		return receivedTokenValue;
	}

	public void setTokenAsCookie(HttpServletResponse response, SecurityContext context) {
		final String tokenKey = properties.getSecurity().getTokenKey();

		final Authentication authentication = context.getAuthentication();
		if (authentication instanceof SecurityAuthenticationToken) {
			final SecurityAuthenticationToken sat = (SecurityAuthenticationToken) authentication;
			String sendTokenValue = sat.getToken();
			final Cookie cookie = new Cookie(tokenKey, sendTokenValue);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(-1);
			response.addCookie(cookie);
		} else {
			final Cookie cookie = new Cookie(tokenKey, "-");
			cookie.setHttpOnly(true);
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}

	}
}
