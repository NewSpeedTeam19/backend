package com.newspeed19.feed.exception;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final int code;
	private final HttpStatus httpStatus;

	@Builder
	public CustomException(ExceptionCode exceptionCode) {
		super(exceptionCode.getMessage());
		this.httpStatus = exceptionCode.getHttpStatus();
		this.code = exceptionCode.getCode();
	}
}
