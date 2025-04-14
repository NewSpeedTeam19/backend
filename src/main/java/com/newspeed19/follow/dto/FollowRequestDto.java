package com.newspeed19.follow.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * 팔로우 요청 또는 취소 요청 시 사용되는 DTO
 */
@Getter
public class FollowRequestDto {
	private final Long targetUserId;
	
	@JsonCreator
	public FollowRequestDto(@JsonProperty("targetUserId") Long targetUserId) {
		this.targetUserId = targetUserId;
	}
}
