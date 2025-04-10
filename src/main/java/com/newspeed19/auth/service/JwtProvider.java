package com.newspeed19.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * @packageName    : com.newspeed19.auth.service
 * @fileName       : JwtProvider
 * @author         : yong
 * @date           : 4/9/25
 * @description    :
 */
@Component
public class JwtProvider {
	@Value("${jwt.secret}")
	private String secretKey;

	private final long ACCESS_TOKEN_VALIDITY = 1000L * 60; // 1분

	public String createToken(long userId, String userType) {
		Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
		Date now = new Date();
		Date expiration = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

		Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public String getUserId(String token) {
		Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	public void validateToken(String token) {
		Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		if (claims.isEmpty()) {
			throw new RuntimeException("validate실패");
		}
	}

	public String getToken(String authHeader) {
		return authHeader.substring(7);
	}

	public long getExiration(String token) {
		Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
		Date expiration = claims.getExpiration();
		return expiration.getTime();
	}
}
