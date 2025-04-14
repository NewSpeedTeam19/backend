package com.newspeed19.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
	@NotBlank
	@Pattern(regexp = "^[0-9a-zA-Z가-힣]{2,10}$", message = "이름은 2이상 10이하여야합니다~")
	private final String name;

	@Email
	@NotBlank
	private final String email;

	@NotBlank
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{12,}$", message = "비밀번호는 대문자, 소문자, 숫자를 모두 포함해야 하며 최소 12자 이상이어야 합니다.")
	private final String password;

	@NotBlank
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{12,}$", message = "비밀번호는 대문자, 소문자, 숫자를 모두 포함해야 하며 최소 12자 이상이어야 합니다.")
	private final String passwordConfirm;
}
