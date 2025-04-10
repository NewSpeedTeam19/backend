package com.newspeed19.comment.exception;

import com.newspeed19.common.exception.CustomException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentException extends CustomException {

	private final CommentErrorCode errorCode;

	@Builder
	public CommentException(CommentErrorCode errorCode) {
		super(errorCode.getMessage(), errorCode.getCode());
		this.errorCode = errorCode;
	}
}
