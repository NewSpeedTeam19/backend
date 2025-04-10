package com.newspeed19.user.exception;

import com.newspeed19.common.exception.CustomException;

import lombok.Builder;

public class UserException extends CustomException {

	private final UserErrorCode errorCode;

	@Builder
	public UserException(UserErrorCode errorCode) {
		super(errorCode.getMessage(), errorCode.getCode());
		this.errorCode = errorCode;
	}
}
