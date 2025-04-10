package com.newspeed19.feed.exception;

import com.newspeed19.common.exception.CustomException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedException extends CustomException {

	private final FeedErrorCode errorCode;

	@Builder
	public FeedException(FeedErrorCode errorCode) {
		super(errorCode.getMessage(), errorCode.getCode());
		this.errorCode = errorCode;
	}
}
