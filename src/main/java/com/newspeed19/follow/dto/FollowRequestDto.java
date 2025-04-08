package com.newspeed19.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 팔로우 요청 또는 취소 요청 시 사용되는 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowRequestDto {
	private Long targetUserId;
}
