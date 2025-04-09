package com.newspeed19.follow.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

/**
 * 받은 팔료우 요청 조회 응답 DTO
 */
@Getter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
public class ReceivedFollowResponseDto {
	private final Long userId;
	private final String username;
	private final String profileImageUrl;
	private final LocalDateTime requestedAt;

	@Builder
	public ReceivedFollowResponseDto(String username, Long userId, String profileImageUrl, LocalDateTime requestedAt) {
		this.username = username;
		this.userId = userId;
		this.profileImageUrl = profileImageUrl;
		this.requestedAt = requestedAt;
	}
}
