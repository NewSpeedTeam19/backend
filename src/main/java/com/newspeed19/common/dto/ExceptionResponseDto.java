package com.newspeed19.common.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonPropertyOrder({"code", "status", "message"})
public class ExceptionResponseDto {
	private final int code; // (ex. 200, 201, 401..)
	private final String status; // (ex. OK, CREATED, NOT_FOUND...)
	private final String message; // ex. 생성이 완료되었습니다.

}
