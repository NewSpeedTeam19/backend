package com.newspeed19.follow.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 팔로우 관련 에러 코드 정의
 */
@Getter
@RequiredArgsConstructor
public enum FollowErrorCode {

	CANNOT_FOLLOW_SELF(400, "자기 자신에게는 팔로우 요청을 보낼 수 없습니다."),
	FOLLOW_ALREADY_EXISTS(409, "팔로우 요청이 이미 전송되었거나 수락되었습니다."),
	FOLLOW_CANCEL_NOT_FOUND(404, "취소할 팔로우 요청이 존재하지 않습니다."),
	FOLLOW_ACCEPT_NOT_FOUND(404, "수락할 팔로우 요청이 존재하지 않습니다."),
	FOLLOW_REJECT_NOT_FOUND(404, "거절할 팔로우 요청이 존재하지 않습니다.");

	private final int code;
	private final String message;
}
