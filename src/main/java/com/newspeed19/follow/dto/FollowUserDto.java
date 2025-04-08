package com.newspeed19.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 팔로워/팔로잉 목록 조회에 사용되는 사용자 정보 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowUserDto {
	private Long userId;
	private String username;
	private String profileImageUrl;
}
