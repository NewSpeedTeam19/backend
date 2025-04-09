package com.newspeed19.user.profile.dto;

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
}