package com.newspeed19.user.profile.service;

import com.newspeed19.user.entity.User;
import com.newspeed19.user.exception.UserErrorCode;
import com.newspeed19.user.exception.UserException;
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
			.feedCount(user.getFeedCount())
			.followCount(user.getFollowCount())
			.followingCount(user.getFollowingCount())
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
			.orElseThrow(() -> UserException.builder().errorCode(UserErrorCode.NOT_FOUND_USER).build());

		return UserProfileResponseDto.builder()
			.id(user.getId())
			.name(user.getName())
			.introduction(user.getIntroduction())
			.image(user.getImage())
			.email(null)
			.feedCount(user.getFeedCount())
			.followCount(user.getFollowCount())
			.followingCount(user.getFollowingCount())
			.build();
	}
	@Override
	public User getUserEntity(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> UserException.builder()
						.errorCode(UserErrorCode.NOT_FOUND_USER)
						.build());
	}

}
