package com.newspeed19.user.profile.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserProfileUpdateRequestDto {

	@Size(min = 1, max = 20)
	private String name;

	@Size(max = 256)
	private String introduction;

	private String image;
}
