package com.newspeed19.user.profile.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponseDto {
	private Long id;
	private String name;
	private String introduction;
	private String image;
	private String email;
}