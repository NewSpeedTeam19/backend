package com.newspeed19.auth.service;

import org.springframework.stereotype.Service;

import com.newspeed19.auth.dto.LoginRequestDto;
import com.newspeed19.auth.dto.SignupRequestDto;
import com.newspeed19.auth.entity.User;
import com.newspeed19.auth.repository.UserRepository;

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
	private final UserRepository repository;
	private final JwtProvider jwtProvider;

	@Transactional
	public void signup(SignupRequestDto dto) {
		User user = new User(dto.getName(), dto.getEmail(), dto.getPassword());
		repository.save(user);
	}

	public boolean checkName(String name) {
		return !repository.existsByName(name);
	}

	public String login(LoginRequestDto dto) {
		User user = repository.findByEmail(dto.getEmail()).orElseThrow();
		if (!user.getPassword().equals(dto.getPassword())) {
			throw new RuntimeException("비번이 틀렸어 로그인 실패!");
		}

		String accessToken = jwtProvider.createToken(user.getId(), "accessToken");
		return accessToken;
	}

	public void reissue(String access, String refresh) {
		String token = jwtProvider.getToken(refresh);
		jwtProvider.validateToken(token);
		String id = jwtProvider.getUserId(token);
		// repository.refresh
	}
}
