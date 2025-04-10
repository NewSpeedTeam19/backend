package com.newspeed19.feed.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FeedErrorCode {
	/**
	 * 3000: Feed 에러
	 */
	FEED_NOT_FOUND(3001, "존재하지 않는 피드입니다.");

	private final int code;
	private final String message;
}
