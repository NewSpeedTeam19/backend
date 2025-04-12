package com.newspeed19.user.profile.service;

import java.util.List;

import com.newspeed19.comment.repository.CommentRepository;
import com.newspeed19.feed.dto.response.FeedPageResponseDto;
import com.newspeed19.feed.dto.response.FeedResponseDto;
import com.newspeed19.feed.entity.Feed;
import com.newspeed19.feed.repository.FeedRepository;
import com.newspeed19.user.entity.User;
import com.newspeed19.user.exception.UserErrorCode;
import com.newspeed19.user.exception.UserException;
import com.newspeed19.user.profile.dto.UserPasswordUpdateRequestDto;
import com.newspeed19.user.profile.dto.UserProfileResponseDto;
import com.newspeed19.user.profile.dto.UserProfileUpdateRequestDto;
import com.newspeed19.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final UserRepository userRepository;
	private final FeedRepository feedRepository;
	private final CommentRepository commentRepository;
	private final BCryptPasswordEncoder passwordEncoder;


	// 내 프로필 조회
	@Override
	public UserProfileResponseDto getMyProfile(User user) {
		FeedPageResponseDto userFeeds = findAllFeedById(user.getId());

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

		FeedPageResponseDto userFeeds = findAllFeedById(user.getId());

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

	// 유저의 모든 피드를 조회
	private FeedPageResponseDto findAllFeedById(Long userId) {
		// 페이징 객체 생성 (수정일자 내림차순, 좋아요 많은 순)
		PageRequest pageable = PageRequest.of(
			0,
			10,
			Sort.by(Sort.Order.desc("updatedAt"), Sort.Order.desc("likes"))
		);

		// 전체 피드 목록 조회
		Page<Feed> feedPage = feedRepository.findAll(pageable);

		// 응답 객체 생성
		Page<FeedResponseDto> feedPageResponseDto = feedPage.map(feed ->
			FeedResponseDto.builder()
				.id(feed.getId())
				.contents(feed.getContents())
				.image(feed.getImage())
				.likes(feed.getLikes())
				.commentCount(commentRepository.countByFeedId(feed.getId()))
				.createdAt(feed.getCreatedAt())
				.updatedAt(feed.getUpdatedAt())
				.build()
		);
		List<FeedResponseDto> feeds = feedPageResponseDto.getContent();

		return FeedPageResponseDto.builder()
			.pages(feedPageResponseDto)
			.feeds(feeds)
			.feedCount(feedRepository.countByUserId(userId))
			.build();
	}


	// 비밀번호 변경
	@Override
	@Transactional
	public void updatePassword(Long userId, UserPasswordUpdateRequestDto request) {
		try {
			User user = getUserEntity(userId); // ✅ 수정된 부분

			String current = request.getCurrentPassword();
			String newPw = request.getNewPassword();

			if (!passwordEncoder.matches(current, user.getPassword())) {
				throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
			}

			if (newPw.length() < 8 || !newPw.matches(".*[A-Za-z].*") || !newPw.matches(".*\\d.*")) {
				throw new IllegalArgumentException("비밀번호는 8자 이상이며, 문자와 숫자를 포함해야 합니다.");
			}

			if (passwordEncoder.matches(newPw, user.getPassword())) {
				throw new IllegalArgumentException("현재 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다.");
			}

			String encodedPw = passwordEncoder.encode(newPw);
			user.setPassword(encodedPw);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}



}
