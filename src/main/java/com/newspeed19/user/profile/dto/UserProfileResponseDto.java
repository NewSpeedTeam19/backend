package com.newspeed19.user.profile.dto;

import com.newspeed19.feed.dto.response.FeedPageResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponseDto {
	private final Long id;
	private final String name;
	private final String introduction;
	private final String image;
	private final String email;
	private final Long feedCount;
	private final Long followCount;
	private final Long followingCount;
	private final FeedPageResponseDto feeds;
}