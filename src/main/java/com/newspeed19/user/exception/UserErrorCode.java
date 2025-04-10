package com.newspeed19.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode {

	USER_ERROR_CODE(000, "여기에 에러코드를 설정해주세요.");

	private final int code;
	private final String message;
}
