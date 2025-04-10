package com.newspeed19.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final int code;

	@Builder
	public CustomException(String message, int code) {
		super(message);
		this.code = code;
	}
}