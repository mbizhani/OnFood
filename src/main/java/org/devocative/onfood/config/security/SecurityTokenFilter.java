package org.devocative.onfood.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.OnFoodProperties;
import org.devocative.onfood.iservice.ISecurityService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.OnCommittedResponseWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class SecurityTokenFilter extends OncePerRequestFilter {
	private final ISecurityService securityService;
	private final OnFoodProperties properties;
	private final SecurityHandler handler;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		final String tokenKey = properties.getSecurity().getTokenKey();
		final String receivedTokenValue = handler.readToken(request);

		final HttpServletResponse newRs = properties.getSecurity().getTokenMedia() == OnFoodProperties.ETokenMedia.cookie ?
			new SaveToCookieResponseWrapper(response, handler) :
			response;

		if (receivedTokenValue != null) {
			log.debug("SecurityTokenFilter: uri=[{}] media=[{}] token=[{}]",
				request.getRequestURI(), properties.getSecurity().getTokenMedia(), receivedTokenValue);
			securityService.authenticateByToken(receivedTokenValue);
		}

		chain.doFilter(request, newRs);
	}

	private static class SaveToCookieResponseWrapper extends OnCommittedResponseWrapper {
		private final SecurityHandler handler;

		public SaveToCookieResponseWrapper(HttpServletResponse response, SecurityHandler handler) {
			super(response);
			this.handler = handler;
		}

		@Override
		protected void onResponseCommitted() {
			final HttpServletResponse response = (HttpServletResponse) getResponse();

			final SecurityContext context = SecurityContextHolder.getContext();
			handler.setTokenAsCookie(response, context);
		}
	}
}
