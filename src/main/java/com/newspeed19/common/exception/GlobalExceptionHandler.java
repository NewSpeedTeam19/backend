package com.newspeed19.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.newspeed19.common.dto.ExceptionResponseDto;

import jakarta.validation.ConstraintViolationException;

/**
 * 전역 예외 처리 핸들러
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * 커스텀 예외 처리 (ex. FollowException)
	 */
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ExceptionResponseDto> handleCustomException(CustomException exception) {
		ExceptionResponseDto response = ExceptionResponseDto.builder()
			.code(exception.getCode())
			.message(exception.getMessage())
			.build();

		return ResponseEntity.status(exception.getCode()).body(response);
	}

	/**
	 * @Valid 유효성 검증 실패 처리
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponseDto> handleValidationException(MethodArgumentNotValidException exception) {
		String errorMessage = exception.getBindingResult().getFieldError().getDefaultMessage();

		ExceptionResponseDto response = ExceptionResponseDto.builder()
			.code(HttpStatus.BAD_REQUEST.value())
			.message(errorMessage)
			.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * @Validated 파라미터 유효성 검증 실패 처리
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionResponseDto> handleConstraintViolation(ConstraintViolationException exception) {
		ExceptionResponseDto response = ExceptionResponseDto.builder()
			.code(HttpStatus.BAD_REQUEST.value())
			.message(exception.getMessage())
			.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * 처리되지 않은 모든 예외 처리
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponseDto> handleUnexpectedException(Exception exception) {
		ExceptionResponseDto response = ExceptionResponseDto.builder()
			.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
			.message("서버 내부 오류가 발생했습니다.")
			.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}