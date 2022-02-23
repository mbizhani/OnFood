package org.devocative.onfood.config.security;

import lombok.RequiredArgsConstructor;
import org.devocative.onfood.OnFoodProperties;
import org.devocative.onfood.iservice.ISecurityService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
//@Component
public class SecurityTokenContextRepository implements SecurityContextRepository {
	private final ISecurityService securityService;
	private final OnFoodProperties properties;
	private final SecurityHandler handler;

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		final HttpServletRequest request = requestResponseHolder.getRequest();
		final String tokenValue = handler.readToken(request);

		if (properties.getSecurity().getTokenMedia() == OnFoodProperties.ETokenMedia.cookie) {
			requestResponseHolder.setResponse(new SaveToCookieResponseWrapper(requestResponseHolder.getResponse(), handler));
		}

		final SecurityContext context = SecurityContextHolder.createEmptyContext();
		if (tokenValue != null) {
			securityService.authenticateByToken(tokenValue, context);
		}
		return context;
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		return true;
	}

	// ------------------------------

	private static class SaveToCookieResponseWrapper extends SaveContextOnUpdateOrErrorResponseWrapper {
		private final SecurityHandler handler;

		public SaveToCookieResponseWrapper(HttpServletResponse response, SecurityHandler handler) {
			super(response, true);
			this.handler = handler;
		}

		@Override
		protected void saveContext(SecurityContext context) {
			final HttpServletResponse response = (HttpServletResponse) getResponse();

			handler.setTokenAsCookie(response, context);
		}
	}

}
