package com.newspeed19.auth.dto;

import lombok.Getter;

/**
 * @packageName    : com.newspeed19.auth.dto
 * @fileName       : LoginRequestDto
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */
@Getter
public class LoginRequestDto {
	private String email;
	private String password;

	public LoginRequestDto(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
