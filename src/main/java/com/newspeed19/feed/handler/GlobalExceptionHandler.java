package com.newspeed19.feed.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.newspeed19.feed.dto.response.ApiResponseDto;
import com.newspeed19.feed.exception.CustomException;

@RestControllerAdvice // FIXME: 추후 공통 예외 핸들러로 빼야될 핸들러
public class GlobalExceptionHandler {

	/**
	 * 공통 예외 핸들러
	 * @param exception 예외 객체
	 * @return 예외정보와 함께 API 응답객체를 반환
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponseDto<Void>> handleException(CustomException exception) {
		// API 응답 객체 생성
		ApiResponseDto<Void> apiResponseDto = ApiResponseDto.<Void>builder()
			.code(exception.getCode())
			.message(exception.getMessage())
			.status(exception.getHttpStatus().getReasonPhrase())
			.build();
		return ResponseEntity.status(exception.getHttpStatus()).body(apiResponseDto);
	}
}
