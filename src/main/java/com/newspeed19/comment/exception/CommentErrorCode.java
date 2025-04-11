package com.newspeed19.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommentErrorCode {
	COMMENT_NOT_FOUN(404, "댓글이 존재하지 않습니다."),
	COMMENT_NOT_ALLOW(401, "접근 권한이 없습니다."),
	COMMENT_CANT_SELF(400, "본인 댓글에 좋아요는 불가능 합니다."),
	COMMENT_ERROR_CODE(000, "여기에 에러코드를 정의해주세요.");

	private final int code;
	private final String message;
}
