package com.newspeed19.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.util.PatternMatchUtils;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.newspeed19.auth.repository.JwtBlackList;
import com.newspeed19.auth.service.JwtProvider;

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
	private JwtProvider jwtProvider;

	public AuthFilter(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	private final Map<String, List> WHITE_LIST = Map.of(
		"POST", List.of("/api/auth/signup", "/api/auth/namecheck", "/api/auth/login", "/api/auth/reissue"),
		"GET", List.of("/api/feed", "/api/feed/*", "/api/feed/*/comments", "/api/profile/*")
	);

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		String requestUri = request.getRequestURI();
		String requestMethod = request.getMethod();
		HttpServletResponse response = (HttpServletResponse)servletResponse;

		if (!isWhiteList(requestUri, requestMethod)) {
			try {
				String token = jwtProvider.getToken(request.getHeader("Authorization"));
				if (isBlacklisted(token)) {
					throw new RuntimeException("사용 중지된 토큰입니다.");
				}
				long id = getUserIdFromToken(token);
				request.setAttribute("userId", id);
			} catch (JWTVerificationException e) {
				throw new RuntimeException("실패~");
			}
		}
		filterChain.doFilter(servletRequest, response);
	}

	public boolean isBlacklisted(String token) {
		Long expiry = JwtBlackList.list.get(token);
		if (expiry == null)
			return false;
		return true;
	}

	private boolean isWhiteList(String uri, String method) {
		List<String> patterns = WHITE_LIST.get(method);
		if (patterns == null)
			return false;

		for (String pattern : patterns) {
			if (PatternMatchUtils.simpleMatch(pattern, uri)) {
				return true;
			}
		}

		return false;
	}

	private long getUserIdFromToken(String token) {
		jwtProvider.validateToken(token);
		return Long.parseLong(jwtProvider.getUserId(token));
	}

}