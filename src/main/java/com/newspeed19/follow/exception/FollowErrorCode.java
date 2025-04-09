package com.newspeed19.follow.exception;

import lombok.Getter;

/**
 * 팔로우 관련 에러 코드 정의
 */
@Getter
public enum FollowErrorCode {
	FOLLOW_ALREADY_EXISTS,
	FOLLOW_NOT_FOUND,
	NO_PENDING_REQUEST
}
