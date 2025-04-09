package com.newspeed19.follow.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 팔로워/팔로잉 목록 조회에 사용되는 사용자 정보 DTO
 */
@Getter
public class FollowUserResponseDto {
	private final Long userId;
	private final String username;
	private final String profileImageUrl;

	@Builder
	public FollowUserResponseDto(Long userId, String username, String profileImageUrl) {
		this.userId = userId;
		this.username = username;
		this.profileImageUrl = profileImageUrl;
	}
}
