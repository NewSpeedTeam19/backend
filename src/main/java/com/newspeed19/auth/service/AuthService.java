package com.newspeed19.auth.service;

import org.springframework.stereotype.Service;

import com.newspeed19.auth.dto.LoginRequestDto;
import com.newspeed19.auth.dto.SignupRequestDto;
import com.newspeed19.auth.entity.Token;
import com.newspeed19.auth.repository.JwtBlackList;
import com.newspeed19.auth.repository.TokenRepository;
import com.newspeed19.user.entity.User;
import com.newspeed19.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * @packageName    : com.newspeed19.auth.service
 * @fileName       : AuthService
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */
@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;
	private final TokenRepository tokenRepository;

	@Transactional
	public void signup(SignupRequestDto dto) {
		User user = new User(dto.getName(), dto.getEmail(), dto.getPassword());
		userRepository.save(user);
	}

	public boolean checkName(String name) {
		return !userRepository.existsByName(name);
	}

	@Transactional
	public String[] login(LoginRequestDto dto) {
		User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
		if (!user.getPassword().equals(dto.getPassword())) {
			throw new RuntimeException("비번이 틀렸어 로그인 실패!");
		}

		String accessToken = jwtProvider.createToken(user.getId(), "access");
		String refreshToken = jwtProvider.createToken(user.getId(), "refresh");
		Token dbtoken = new Token(refreshToken);
		tokenRepository.save(dbtoken);
		return new String[] {accessToken, refreshToken};
	}

	public String reissue(String refresh) {
		if (!tokenRepository.existsByToken(refresh)) {
			throw new RuntimeException("Db에 토큰이 없어요~");
		}

		long id = Long.parseLong(jwtProvider.getUserId(refresh));
		String accessToken = jwtProvider.createToken(id, "access");

		return accessToken;
	}

	@Transactional
	public void logout(String refreshtoken, String accessToken) {
		String idFromAccess = jwtProvider.getUserId(accessToken);
		String idFromRefresh = jwtProvider.getUserId(refreshtoken);
		if (idFromRefresh.equals(idFromAccess)) {
			JwtBlackList.list.put(accessToken, jwtProvider.getExiration(accessToken));
			tokenRepository.deleteByToken(refreshtoken);
		} else {
			throw new RuntimeException("토큰들이 일치하지 않습니다.");
		}
	}
}
