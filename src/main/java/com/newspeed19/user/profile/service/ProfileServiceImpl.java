package com.newspeed19.user.profile.service;

import com.newspeed19.user.entity.User;
import com.newspeed19.user.profile.dto.UserProfileResponseDto;
import com.newspeed19.user.profile.dto.UserProfileUpdateRequestDto;
import com.newspeed19.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final UserRepository userRepository;

	// 내 프로필 조회
	@Override
	public UserProfileResponseDto getMyProfile(User user) {
		return UserProfileResponseDto.builder()
			.id(user.getId())
			.name(user.getName())
			.introduction(user.getIntroduction())
			.image(user.getImage())
			.email(user.getEmail())
			.build();
	}

	// 내 프로필 수정
	@Override
	@Transactional
	public void updateMyProfile(User user, UserProfileUpdateRequestDto request) {
		user.updateProfile(
			request.getName(),
			request.getIntroduction(),
			request.getImage()
		);
	}

	// 다른 사람 프로필 조회
	@Override
	public UserProfileResponseDto getUserProfile(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

		return UserProfileResponseDto.builder()
			.id(user.getId())
			.name(user.getName())
			.introduction(user.getIntroduction())
			.image(user.getImage())
			.email(null)
			.build();
	}
}
