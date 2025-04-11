package com.newspeed19.user.profile.service;

import com.newspeed19.feed.dto.response.FeedPageResponseDto;
import com.newspeed19.feed.service.FeedService;
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
	private final FeedService feedService;

	// 내 프로필 조회
	@Override
	public UserProfileResponseDto getMyProfile(User user) {
		FeedPageResponseDto userFeeds = feedService.findAllFeedById(user.getId());

		return UserProfileResponseDto.builder()
			.id(user.getId())
			.name(user.getName())
			.introduction(user.getIntroduction())
			.image(user.getImage())
			.email(user.getEmail())
			.feeds(userFeeds)
			.feedCount(userFeeds.getFeedCount())
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

		FeedPageResponseDto userFeeds = feedService.findAllFeedById(user.getId());

		return UserProfileResponseDto.builder()
			.id(user.getId())
			.name(user.getName())
			.introduction(user.getIntroduction())
			.image(user.getImage())
			.email(null)
			.feeds(userFeeds)
			.feedCount(userFeeds.getFeedCount())
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
