package com.newspeed19.user.profile.dto;

import lombok.Getter;

@Getter
public class UserPasswordUpdateRequestDto {
	private String currentPassword;
	private String newPassword;
}
