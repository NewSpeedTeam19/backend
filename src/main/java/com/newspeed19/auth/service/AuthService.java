package com.newspeed19.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.newspeed19.auth.dto.LoginRequestDto;
import com.newspeed19.auth.dto.SignupRequestDto;
import com.newspeed19.auth.entity.Token;
import com.newspeed19.auth.exception.AuthErrorCode;
import com.newspeed19.auth.exception.AuthException;
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
	private final BCryptPasswordEncoder passwordEncoder;


	@Transactional
	public void signup(SignupRequestDto dto) {
		if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
			throw AuthException.builder().errorCode(AuthErrorCode.PASSWORD_MISMATCH).build();
		}

		// 비밀번호 암호화
		String encodedPw = passwordEncoder.encode(dto.getPassword());

		User user = new User(dto.getName(), dto.getEmail(), encodedPw);
		userRepository.save(user);
	}


	public void checkName(String name) {
		if (userRepository.existsByName(name)) {
			AuthException.builder().errorCode(AuthErrorCode.DUPLICATED_NAME).build();
		}
	}

	@Transactional
	public String[] login(LoginRequestDto dto) {
		User user = userRepository.findByEmail(dto.getEmail())
			.orElseThrow(() -> AuthException.builder().errorCode(AuthErrorCode.NOT_FOUND_USER).build());
		if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
			throw AuthException.builder().errorCode(AuthErrorCode.WRONG_PASSWORD).build();
		}

		String accessToken = jwtProvider.createToken(user.getId(), "access");
		String refreshToken = jwtProvider.createToken(user.getId(), "refresh");

		tokenRepository.save(new Token(refreshToken));
		return new String[] {accessToken, refreshToken};
	}

	public String reissue(String refresh) {
		if (!tokenRepository.existsByToken(refresh)) {
			throw AuthException.builder().errorCode(AuthErrorCode.WRONG_TOKEN).build();
		}

		long id = Long.parseLong(jwtProvider.getUserId(refresh));
		return jwtProvider.createToken(id, "access");
	}

	@Transactional
	public void logout(String refreshtoken, String accessToken) {
		String idFromAccess = jwtProvider.getUserId(accessToken);
		String idFromRefresh = jwtProvider.getUserId(refreshtoken);
		if (!idFromRefresh.equals(idFromAccess)) {
			throw AuthException.builder().errorCode(AuthErrorCode.TOKEN_MISMATCH).build();
		}

		JwtBlackList.list.put(accessToken, jwtProvider.getExiration(accessToken));
		tokenRepository.deleteByToken(refreshtoken);
	}
}
