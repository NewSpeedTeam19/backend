package com.newspeed19.common.exception;

import java.security.SignatureException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.newspeed19.common.dto.ExceptionResponseDto;

import io.jsonwebtoken.MalformedJwtException;
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
		// CustomException 에 code 가 null 일 경우, 500 에러
		HttpStatus httpStatus = Optional.ofNullable(HttpStatus.resolve(exception.getCode()))
			.orElse(HttpStatus.INTERNAL_SERVER_ERROR);

		// 예외 응답 DTO 생성
		ExceptionResponseDto response = ExceptionResponseDto.builder()
			.code(exception.getCode())
			.status(httpStatus.getReasonPhrase())
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
			.status(HttpStatus.BAD_REQUEST.getReasonPhrase())
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
			.status(HttpStatus.BAD_REQUEST.getReasonPhrase())
			.message(exception.getMessage())
			.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	/**
	 * 잘못된 토큰이 입력되었을 때 위한 예외 처리
	 */
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ExceptionResponseDto> handleSignatureException(SignatureException exception) {
		ExceptionResponseDto response = ExceptionResponseDto.builder()
			.code(HttpStatus.BAD_REQUEST.value())
			.status(HttpStatus.BAD_REQUEST.getReasonPhrase())
			.message(exception.getMessage())
			.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	/**
	 * 잘못된 토큰을 입력했을 때를 위한 예외 처리
	 */
	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<ExceptionResponseDto> handleMalformedJwtException(MalformedJwtException exception) {
		ExceptionResponseDto response = ExceptionResponseDto.builder()
			.code(HttpStatus.BAD_REQUEST.value())
			.status(HttpStatus.BAD_REQUEST.getReasonPhrase())
			.message(exception.getMessage())
			.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	/**
	 * 처리되지 않은 모든 예외 처리
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponseDto> handleUnexpectedException(Exception exception) {
		ExceptionResponseDto response = ExceptionResponseDto.builder()
			.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
			.status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
			.message("서버 내부 오류가 발생했습니다.")
			.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}