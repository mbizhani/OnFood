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
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class SecurityTokenFilter extends OncePerRequestFilter {
	private final ISecurityService securityService;
	private final OnFoodProperties properties;
	private final SecurityHandler handler;

	// ------------------------------

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		final String receivedTokenValue = handler.readToken(request);

		final HttpServletResponse finalResponse = properties.getSecurity().getTokenMedia() == OnFoodProperties.ETokenMedia.cookie ?
			new AddCookieToResponseWrapper(response, handler) :
			response;

		if (receivedTokenValue != null) {
			log.debug("SecurityTokenFilter: uri=[{}] media=[{}] token=[{}]",
				request.getRequestURI(), properties.getSecurity().getTokenMedia(), receivedTokenValue);
			securityService.authenticateByToken(receivedTokenValue);
		}

		final HttpServletRequest finalRequest;

		if ("base64".equals(request.getHeader("X-Body-Format"))) {
			final String body = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))
				.lines()
				.collect(Collectors.joining("\n"));

			final byte[] decodedBody = Base64.getMimeDecoder().decode(body.getBytes(StandardCharsets.UTF_8));
			finalRequest = new ByteArrayBodyRequestWrapper(request, decodedBody);
		} else {
			finalRequest = request;
		}

		chain.doFilter(finalRequest, finalResponse);
	}

	// ------------------------------

	private static class ByteArrayBodyRequestWrapper extends HttpServletRequestWrapper {
		private final byte[] body;

		// ---------------

		public ByteArrayBodyRequestWrapper(HttpServletRequest request, byte[] body) {
			super(request);
			this.body = body;
		}

		// ---------------

		@Override
		public ServletInputStream getInputStream() throws IOException {
			return new ByteArrayServletInputStream(body);
		}
	}

	private static class ByteArrayServletInputStream extends ServletInputStream {
		private final ByteArrayInputStream delegate;

		// ---------------

		public ByteArrayServletInputStream(byte[] bytes) {
			delegate = new ByteArrayInputStream(bytes);
		}

		// ---------------

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int read() {
			return delegate.read();
		}

		@Override
		public int read(byte[] b, int off, int len) {
			return delegate.read(b, off, len);
		}

		@Override
		public int read(byte[] b) throws IOException {
			return delegate.read(b);
		}

		@Override
		public long skip(long n) {
			return delegate.skip(n);
		}

		@Override
		public int available() {
			return delegate.available();
		}

		@Override
		public void close() throws IOException {
			delegate.close();
		}

		@Override
		public synchronized void mark(int readLimit) {
			delegate.mark(readLimit);
		}

		@Override
		public synchronized void reset() {
			delegate.reset();
		}

		@Override
		public boolean markSupported() {
			return delegate.markSupported();
		}
	}

	private static class AddCookieToResponseWrapper extends OnCommittedResponseWrapper {
		private final SecurityHandler handler;

		// ---------------

		public AddCookieToResponseWrapper(HttpServletResponse response, SecurityHandler handler) {
			super(response);
			this.handler = handler;
		}

		// ---------------

		@Override
		protected void onResponseCommitted() {
			final HttpServletResponse response = (HttpServletResponse) getResponse();

			final SecurityContext context = SecurityContextHolder.getContext();
			handler.setTokenAsCookie(response, context);
		}
	}
}
