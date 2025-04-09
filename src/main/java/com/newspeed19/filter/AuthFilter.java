package com.newspeed19.filter;

import java.io.IOException;

import org.springframework.util.PatternMatchUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @packageName    : com.newspeed19.filter
 * @fileName       : AuthFilter
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */

public class AuthFilter implements Filter {
	private final String secretKey;

	public AuthFilter(String secretKey) {
		this.secretKey = secretKey;
	}

	private final String[] WHITE_LIST = {"/api/auth/signup", "/api/auth/namecheck", "/api/auth/login", "/api/feed",
		"/api/feed/{id}",
		"/api/profile/{id}", "/api/feed/{id}/comments"};

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		String requestUri = request.getRequestURI();
		HttpServletResponse response = (HttpServletResponse)servletResponse;

		if (!isWHITE_LIST(requestUri)) {
			try {
				String token = request.getHeader("Authorization").substring(7);
				JWT.require(Algorithm.HMAC512(secretKey))
					.build().verify(token);
			} catch (JWTVerificationException e) {
				throw new RuntimeException("실패~");
			}
		}
		filterChain.doFilter(servletRequest, response);
	}

	public boolean isWHITE_LIST(String uri) {
		return PatternMatchUtils.simpleMatch(WHITE_LIST, uri);
	}
}
