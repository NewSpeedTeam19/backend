package com.newspeed19.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonPropertyOrder({"code", "status", "message", "data"})
public class ApiResponseDto<T> {
	private final int code; // (ex. 200, 201, 401..)
	private final String status; // (ex. OK, CREATED, NOT_FOUND...)
	private final String message; // ex. 생성이 완료되었습니다.

	@JsonInclude(JsonInclude.Include.NON_NULL) // null 값이 아닌 경우에만 출력
	private final T data;
}
