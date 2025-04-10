package com.newspeed19.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommentErrorCode {
	COMMENT_ERROR_CODE(000, "여기에 에러코드를 정의해주세요.");

	private final int code;
	private final String message;
}
