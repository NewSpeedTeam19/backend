package com.newspeed19.follow.dto;

import com.newspeed19.follow.entity.FollowStatus;

import lombok.Builder;
import lombok.Getter;

/**
 * 팔로우 요청 응답 DTO
 */
@Getter
public class FollowResponseDto {
	private final String message;
	private final FollowStatus status;
	private final Long targetUserId;

	@Builder
	public FollowResponseDto(String message, FollowStatus status, Long targetUserId) {
		this.message = message;
		this.status = status;
		this.targetUserId = targetUserId;
	}
}
