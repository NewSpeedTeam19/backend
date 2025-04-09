package com.newspeed19.filter;

import java.io.IOException;

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

		if (!isWhiteList(requestUri)) {
			try {
				String token = jwtProvider.getToken(request.getHeader("Authorization"));
				if (isBlacklisted(token)) {
					throw new RuntimeException("사용 중지된 토큰입니다.");
				}
				jwtProvider.validateToken(token);
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

	private boolean isWhiteList(String uri) {
		return PatternMatchUtils.simpleMatch(WHITE_LIST, uri);
	}
}

// access token, refreshtoken 둘 다 확인했을 때 통과할 시
// redis