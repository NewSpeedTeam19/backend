package com.newspeed19.feed.dto.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Getter;

/**
 * API 공통응답 DTO
 * @param <T> 응답할 데이터의 타입
 *
 */
@Getter
@Builder
@JsonPropertyOrder({"success", "message", "httpStatus", "data"})
public class ApiResponseDto<T> {
	private final boolean success;
	private final String message;
	private final HttpStatus httpStatus;
	private final T data;
}
