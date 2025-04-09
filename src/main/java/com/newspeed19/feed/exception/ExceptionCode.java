package com.newspeed19.feed.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
	/**
	 * 3000: Feed 에러
	 */
	FEED_NOT_FOUND(3001, HttpStatus.NOT_FOUND, "존재하지 않는 피드입니다.");

	private final int code;
	private final HttpStatus httpStatus;
	private final String message;
}
