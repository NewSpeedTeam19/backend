package com.newspeed19.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode {

	AUTH_ERROR_CODE(0000, "여기에 에러코드를 정의해주세요.");

	private final int code;
	private final String message;
}
