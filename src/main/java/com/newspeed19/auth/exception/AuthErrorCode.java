package com.newspeed19.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode {

	PASSWORD_MISMATCH(9001, "비밀번호와 비밀번호 재확인이 일치하지 않습니다."),
	NOT_FOUND_USER(9002, "이메일에 해당하는 정보를 찾지 못하여 로그인에 실패했습니다."),
	WRONG_PASSWORD(9003, "비밀번호가 틀렸습니다."),
	WRONG_TOKEN(9004, "DB에 존재하지 않는 토큰입니다."),
	TOKEN_MISMATCH(9005, "accessToken과 refreshToken의 정보가 일치하지 않습니다."),
	DUPLICATED_NAME(9006, "중복된 이름입니다."),
	ACCESS_IS_EMPTY(9007, "accessToken이 비어있습니다.");

	private final int code;
	private final String message;
}