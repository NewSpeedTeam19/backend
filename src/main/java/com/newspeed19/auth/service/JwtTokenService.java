package com.newspeed19.auth.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * @packageName    : com.newspeed19.auth.service
 * @fileName       : JwtTokenService
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */
@Service
public class JwtTokenService {
	@Value("${jwt.secret}")
	private String secretKey;

	public String createToken(Long id, String nickname, int expMinutes, String tokenType) {
		Date accessTokenExp = new Date(System.currentTimeMillis() + (60000 * expMinutes));

		String createdToken = JWT.create()
			.withSubject(String.valueOf(id))
			.withClaim("nickname", nickname)
			.withClaim("tokenType", tokenType)
			.withExpiresAt(accessTokenExp)
			.sign(Algorithm.HMAC512(secretKey));

		return createdToken;
	}

	// public DecodedJWT verifyToken(String token) {
	// 	return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
	// }
}
