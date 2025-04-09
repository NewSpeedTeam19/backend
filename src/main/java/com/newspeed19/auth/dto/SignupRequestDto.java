package com.newspeed19.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @packageName    : com.newspeed19.auth.dto
 * @fileName       : SignupRequestDto
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */
@RequiredArgsConstructor
@Getter
public class SignupRequestDto {
	private final String name;
	private final String email;
	private final String password;
}
