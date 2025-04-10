package com.newspeed19.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newspeed19.auth.dto.LoginRequestDto;
import com.newspeed19.auth.dto.SignupRequestDto;
import com.newspeed19.auth.repository.JwtBlackList;
import com.newspeed19.auth.service.AuthService;
import com.newspeed19.auth.service.JwtProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * @packageName    : com.newspeed19.auth
 * @fileName       : AuthController
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final JwtProvider jwtProvider;

	@PostMapping("/signup")
	public void signup(@RequestBody SignupRequestDto dto) {
		authService.signup(dto);
	}

	@PostMapping("/namecheck")
	public ResponseEntity<String> checkName(@RequestParam String name) {
		if (authService.checkName(name)) {
			return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 닉네임입니다.");
		}
		throw new RuntimeException("사용 중인 닉네임입니다.");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
		String accessToken = authService.login(dto);
		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, accessToken).body("로그인 성공");
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
		String token = jwtProvider.getToken(authHeader);
		// 토큰이 있는지
		JwtBlackList.list.put(token, jwtProvider.getExiration(token));
		return ResponseEntity.status(HttpStatus.OK).body("로그아웃 성공");
	}

	@PostMapping("/reissue")
	public void reissue() {
		String refresh = "refresh";
		String access = "access";
		authService.reissue(access, refresh);

	}

	@GetMapping("/test")
	public void test(HttpServletRequest req) {
		long userId = (Long)req.getAttribute("userId");
		System.out.println(userId);
	}
}

