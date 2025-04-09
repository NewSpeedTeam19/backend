package com.newspeed19.follow.dto;

import com.newspeed19.follow.entity.FollowStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 팔로우 요청 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowResponseDto {
	private String message;
	private FollowStatus status;
	private Long targetUserId;
}
