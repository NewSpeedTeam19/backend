package com.newspeed19.user.profile.service;

import com.newspeed19.user.entity.User;
import com.newspeed19.user.profile.dto.UserPasswordUpdateRequestDto;
import com.newspeed19.user.profile.dto.UserProfileResponseDto;
import com.newspeed19.user.profile.dto.UserProfileUpdateRequestDto;

public interface ProfileService {

	// 내 프로필 조회
	UserProfileResponseDto getMyProfile(User user);

	// 내 프로필 수정
	void updateMyProfile(User user, UserProfileUpdateRequestDto request);

	// 다른 사람 프로필 조회
	UserProfileResponseDto getUserProfile(Long userId);

	// Id로 유저 조회
	User getUserEntity(Long userId);

	// 비밀번호 변경
	void updatePassword(Long userId, UserPasswordUpdateRequestDto request);


}
