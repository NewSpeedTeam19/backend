package com.newspeed19.follow.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 받은 팔료우 요청 조회 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivedFollowRequestDto {
	private Long userId;
	private String username;
	private String profileImageUrl;
	private LocalDateTime requestedAt;
}
