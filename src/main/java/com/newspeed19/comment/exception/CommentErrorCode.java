package com.newspeed19.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommentErrorCode {
	COMMENT_NOT_FOUN(400, "댓글이 없습니다."),
	COMMENT_NOT_ALLOW(500, "접근 권한이 없습니다."),
	COMMENT_CANT_SELF(500, "본인 글에 좋아요는 불가능 합니다."),
	COMMENT_ERROR_CODE(000, "여기에 에러코드를 정의해주세요.");

	private final int code;
	private final String message;
}
