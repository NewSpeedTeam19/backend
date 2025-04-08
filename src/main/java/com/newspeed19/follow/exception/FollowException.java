package com.newspeed19.follow.exception;

import lombok.Getter;

/**
 * 팔로우 도메인 전용 예외
 */
@Getter
public class FollowException extends RuntimeException {

	private final FollowErrorCode errorCode;

	public FollowException(FollowErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
