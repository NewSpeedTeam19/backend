package com.newspeed19.follow.exception;

import com.newspeed19.common.exception.CustomException;

import lombok.Getter;

/**
 * 팔로우 도메인 전용 예외
 */
@Getter
public class FollowException extends CustomException {

	private final FollowErrorCode errorCode;

	public FollowException(FollowErrorCode errorCode) {
		super(errorCode.getMessage(), errorCode.getCode());
		this.errorCode = errorCode;
	}
}
