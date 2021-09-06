package org.devocative.onfood.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.iservice.ISecurityService;
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
public class JwtFilter extends OncePerRequestFilter {
	private final ISecurityService securityService;

	@Override
	protected void doFilterInternal(HttpServletRequest rq, HttpServletResponse rs, FilterChain chain) throws ServletException, IOException {
		final String authorization = rq.getHeader("Authorization");
		if (authorization != null) {
			log.debug("JwtFilter: uri=[{}] token=[{}]", rq.getRequestURI(), authorization);
			securityService.authenticateByToken(authorization);
		}
		chain.doFilter(rq, rs);
	}

}
