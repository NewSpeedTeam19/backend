package com.newspeed19.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode {

	NOT_FOUND_USER(401,"유저를 찾을 수 없습니다.");

	private final int code;
	private final String message;
}
