package com.newspeed19.feed.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.newspeed19.comment.dto.response.CommentResponseDto;
import com.newspeed19.comment.repository.CommentLikeRepository;
import com.newspeed19.comment.repository.CommentRepository;
import com.newspeed19.feed.dto.request.FeedRequestDto;
import com.newspeed19.feed.dto.response.FeedDetailResponseDto;
import com.newspeed19.feed.dto.response.FeedPageResponseDto;
import com.newspeed19.feed.dto.response.FeedResponseDto;
import com.newspeed19.feed.entity.Feed;
import com.newspeed19.feed.exception.FeedErrorCode;
import com.newspeed19.feed.exception.FeedException;
import com.newspeed19.feed.repository.FeedRepository;
import com.newspeed19.follow.repository.FollowRepository;
import com.newspeed19.user.entity.User;
import com.newspeed19.user.profile.dto.UserProfileResponseDto;
import com.newspeed19.user.profile.service.ProfileServiceImpl;
import com.newspeed19.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {
	private final ProfileServiceImpl profileService;

	private final FeedRepository feedRepository;
	private final FollowRepository followRepository;
	private final CommentRepository commentRepository;
	private final CommentLikeRepository commentLikeRepository;
	private final UserRepository userRepository;

	/**
	 * [Service] 전체 피드를 조회하는 메서드
	 * @param page 페이지 번호
	 * @param size 페이지 크기
	 * @return 페이징된 피드 응답객체를 반환
	 */
	@Transactional(readOnly = true)
	public FeedPageResponseDto findAllFeed(Long userId, int page, int size) {
		// 페이징 객체 생성
		int adjustedPage = (page > 0) ? page - 1 : 0;
		PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("updatedAt").descending());

		// 내가 팔로우한 유저 ID 리스트 조회
		List<Long> followingIds = followRepository.findByFollowingUserIds(userId);

		Page<FeedResponseDto> feedPageResponseDto;

		// 팔로우가 없을 경우, 전체 피드 목록 조회

		if (followingIds.isEmpty()) {
			feedPageResponseDto = feedRepository.findAll(pageable)
				.map(feed ->
					// FIXME: likes 카운트 넣어야 됨
					FeedResponseDto.builder()
						.id(feed.getId())
						.contents(feed.getContents())
						.image(feed.getImage())
						.commentCount(commentRepository.countByFeedId(feed.getId()))
						.createdAt(feed.getCreatedAt())
						.updatedAt(feed.getUpdatedAt())
						.build()
				);
		} else { // 팔로우가 있을 경우, 팔로우한 사람들의 피드 목록 조회
			feedPageResponseDto = feedRepository.findByUserIdIn(followingIds, pageable)
				.map(feed ->
					// FIXME: likes 카운트 넣어야 됨
					FeedResponseDto.builder()
						.id(feed.getId())
						.contents(feed.getContents())
						.image(feed.getImage())
						.commentCount(commentRepository.countByFeedId(feed.getId()))
						.createdAt(feed.getCreatedAt())
						.updatedAt(feed.getUpdatedAt())
						.build()
				);
		}

		// 응답 객체 생성
		List<FeedResponseDto> feeds = feedPageResponseDto.getContent();

		return FeedPageResponseDto.builder()
			.pages(feedPageResponseDto)
			.feeds(feeds)
			.build();
	}

	/**
	 * [Service] 단일 피드를 조회하는 메서드
	 * @param id 피드 id
	 * @return 상세 피드 응답객체를 반환
	 */
	@Transactional(readOnly = true)
	public FeedDetailResponseDto findFeedById(Long id) {
		Feed feed = feedRepository.findById(id)
			.orElseThrow(() -> FeedException
				.builder()
				.errorCode(FeedErrorCode.FEED_NOT_FOUND)
				.build());

		// 피드에 달린 댓글 목록
		List<CommentResponseDto> comments = commentRepository.findAllByFeedId(id).stream()
			.map(comment ->
				CommentResponseDto.toDto(comment, commentLikeRepository.countByCommentId(comment.getId())))
			.toList();

		// 유저 DTO
		UserProfileResponseDto myProfile = profileService.getMyProfile(feed.getUser());

		return FeedDetailResponseDto.builder()
			// FIXME: likes 카운트 넣어야 됨
			.id(feed.getId())
			.contents(feed.getContents())
			.image(feed.getImage())
			.commentCount(commentRepository.countByFeedId(feed.getId()))
			.createdAt(feed.getCreatedAt())
			.updatedAt(feed.getUpdatedAt())
			.user(myProfile)
			.comments(comments)
			.build();
	}

	/**
	 * [Service] 피드를 생성하는 메서드
	 * @param loginUserId 로그인 유저 id
	 * @param dto 사용자 요청 DTO
	 * @return 생성된 상세피드 응답객체를 반환
	 */
	@Transactional
	public FeedDetailResponseDto createFeed(Long loginUserId, FeedRequestDto dto) {
		// 로그인 유저
		User user = userRepository.getByIdOrThrow(loginUserId);

		// 유저 DTO
		UserProfileResponseDto myProfile = profileService.getMyProfile(user);

		// 피드 생성
		Feed feed = feedRepository.save(
			Feed.builder()
				.contents(dto.getContents())
				.image(dto.getImage())
				.user(user)
				.build()
		);

		return FeedDetailResponseDto.builder()
			.id(feed.getId())
			.contents(feed.getContents())
			.image(feed.getImage())
			.createdAt(feed.getCreatedAt())
			.updatedAt(feed.getUpdatedAt())
			.user(myProfile)
			.build();
	}

	/**
	 * [Service] 피드를 수정하는 메서드
	 * @param dto 사용자 요청 DTO
	 * @param loginUserId 로그인 유저 id
	 * @param id 피드 id
	 * @return 수정된 상세피드 응답객체를 반환
	 */
	@Transactional
	public FeedDetailResponseDto updateFeed(FeedRequestDto dto, Long loginUserId, Long id) {
		Feed feed = feedRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."));

		// 로그인 유저가 작성한 피드가 아닐 경우 예외 처리
		if (!loginUserId.equals(feed.getUser().getId())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
		}

		// 로그인 유저
		User user = userRepository.getByIdOrThrow(loginUserId);

		// 유저 DTO
		UserProfileResponseDto myProfile = profileService.getMyProfile(user);

		// 피드에 달린 모든 댓글 가져오기
		List<CommentResponseDto> comments = commentRepository.findAllByFeedId(id).stream()
			.map(comment ->
				CommentResponseDto.toDto(comment, commentLikeRepository.countByCommentId(comment.getId())))
			.toList();

		// Feed 업데이트
		feed.updateFeed(dto.getImage(), dto.getContents());

		// FIXME: feedLikes 좋아요 개수 넣어야 됨
		return FeedDetailResponseDto.builder()
			.id(feed.getId())
			.contents(feed.getContents())
			.image(feed.getImage())
			.createdAt(feed.getCreatedAt())
			.updatedAt(feed.getUpdatedAt())
			.user(myProfile)
			.commentCount(commentRepository.countByFeedId(feed.getId()))
			.comments(comments)
			.build();
	}

	/**
	 * [Service] 피드를 삭제하는 메서드
	 * @param loginUserId 로그인 유저 id
	 * @param feedId 피드 Id
	 * @return 업데이트된 페이징 피드 응답객체를 반환
	 */
	@Transactional
	public FeedPageResponseDto deleteFeed(Long loginUserId, Long feedId) {
		Feed feed = feedRepository.findById(feedId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."));

		// 로그인 유저가 작성한 피드가 아닐 경우 예외 처리
		if (!loginUserId.equals(feed.getUser().getId())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
		}

		feedRepository.delete(feed);
		return this.findAllFeed(loginUserId, 0, 10);
	}
}
