package com.newspeed19.feed.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FeedErrorCode {
	FEED_NOT_FOUND(404, "존재하지 않는 피드입니다."),
	FEED_FORBIDDEN(403, "피드에 권한이 없습니다."),
	FEED_MY_FEED_NO_LIKES(400, "본인 글에 좋아요는 불가능합니다.");

	private final int code;
	private final String message;
}