package com.newspeed19.auth.exception;

import com.newspeed19.common.exception.CustomException;

import lombok.Builder;

public class AuthException extends CustomException {

	private final AuthErrorCode errorCode;

	@Builder
	public AuthException(AuthErrorCode errorCode) {
		super(errorCode.getMessage(), errorCode.getCode());
		this.errorCode = errorCode;
	}
}
