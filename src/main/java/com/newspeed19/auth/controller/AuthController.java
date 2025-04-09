package com.newspeed19.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newspeed19.auth.dto.LoginRequestDto;
import com.newspeed19.auth.dto.SignupRequestDto;
import com.newspeed19.auth.service.AuthService;
import com.newspeed19.auth.service.JwtTokenService;

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
	private final JwtTokenService jwtTokenService;

	@PostMapping("/signup")
	public void signup(@RequestBody SignupRequestDto dto) {
		authService.signup(dto);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
		String accessToken = authService.login(dto);

		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, accessToken).body("성공");
	}
	
	@GetMapping("/test")
	public void test(@RequestHeader("Authorization") String authorization) {
		String token = authorization.substring(7);
		System.out.println(token);

		System.out.println();
		System.out.println("filter 확인용");

		// jwtTokenService.checkAvailableToken();
	}
}
