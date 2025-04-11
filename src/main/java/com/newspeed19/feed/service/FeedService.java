package com.newspeed19.feed.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newspeed19.comment.dto.response.CommentResponseDto;
import com.newspeed19.comment.repository.CommentLikeRepository;
import com.newspeed19.comment.repository.CommentRepository;
import com.newspeed19.feed.dto.request.FeedRequestDto;
import com.newspeed19.feed.dto.response.FeedDetailResponseDto;
import com.newspeed19.feed.dto.response.FeedPageResponseDto;
import com.newspeed19.feed.dto.response.FeedResponseDto;
import com.newspeed19.feed.entity.Feed;
import com.newspeed19.feed.entity.FeedLike;
import com.newspeed19.feed.exception.FeedErrorCode;
import com.newspeed19.feed.exception.FeedException;
import com.newspeed19.feed.repository.FeedLikeRepository;
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
	private final FeedLikeRepository feedLikeRepository;
	private final CommentLikeRepository commentLikeRepository;
	private final UserRepository userRepository;

	/**
	 * [Service] 전체 피드를 조회하는 메서드
	 * @param page 페이지 번호
	 * @param size 페이지 크기
	 * @return 페이징된 피드 응답객체를 반환
	 */
	@Transactional(readOnly = true)
	public FeedPageResponseDto findAllFeed(LocalDate start, LocalDate end, Long userId, int page, int size) {
		// 기간별 검색 (기본값: 최근 1개월)
		LocalDateTime[] dates = getDefaultDate(start, end);
		LocalDateTime startDate = dates[0]; // 시작 날짜
		LocalDateTime endDate = dates[1]; // 마지막 날짜

		// 페이징 객체 생성 (수정일자 내림차순, 좋아요 많은 순)
		int adjustedPage = Math.max(page - 1, 0); // 페이지 번호요청 n이 들어오면 n-1번째 페이지 출력
		PageRequest pageable = PageRequest.of(
			adjustedPage,
			size,
			Sort.by(Sort.Order.desc("updatedAt"), Sort.Order.desc("likes"))
		);

		// 내가 팔로우한 유저 ID 리스트 조회
		List<Long> followingIds = followRepository.findByFollowingUserIds(userId);

		// 팔로우가 없을 경우, 전체 피드 목록 조회
		Page<Feed> feedPage = followingIds.isEmpty()
			? feedRepository.findAllByCreatedAtBetween(startDate, endDate, pageable)
			: feedRepository.findByUserIdInAndCreatedAtBetween(followingIds, startDate, endDate, pageable);

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
			.feedCount(feedRepository.count())
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
			.id(feed.getId())
			.contents(feed.getContents())
			.image(feed.getImage())
			.likes(feedLikeRepository.countByFeedId(feed.getId()))
			.commentCount(feed.getLikes())
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
		user.incrementFeedCount();

		return FeedDetailResponseDto.builder()
			.id(feed.getId())
			.contents(feed.getContents())
			.image(feed.getImage())
			.comments(List.of())
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
			.orElseThrow(() -> FeedException.builder()
				.errorCode(FeedErrorCode.FEED_NOT_FOUND)
				.build());

		// 로그인 유저가 작성한 피드가 아닐 경우 예외 처리
		if (!loginUserId.equals(feed.getUser().getId())) {
			throw FeedException.builder().errorCode(FeedErrorCode.FEED_FORBIDDEN).build();
		}

		// 유저 DTO
		UserProfileResponseDto myProfile = profileService.getMyProfile(feed.getUser());

		// 피드에 달린 모든 댓글 가져오기
		List<CommentResponseDto> comments = commentRepository.findAllByFeedId(id).stream()
			.map(comment ->
				CommentResponseDto.toDto(comment, commentLikeRepository.countByCommentId(comment.getId())))
			.toList();

		// Feed 업데이트
		feed.updateFeed(dto.getImage(), dto.getContents());
		return FeedDetailResponseDto.builder()
			.id(feed.getId())
			.contents(feed.getContents())
			.image(feed.getImage())
			.createdAt(feed.getCreatedAt())
			.updatedAt(feed.getUpdatedAt())
			.user(myProfile)
			.likes(feed.getLikes())
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
			.orElseThrow(() -> FeedException.builder()
				.errorCode(FeedErrorCode.FEED_NOT_FOUND)
				.build());
		User loginUser = userRepository.getByIdOrThrow(loginUserId);

		// 로그인 유저가 작성한 피드가 아닐 경우 예외 처리
		if (!loginUserId.equals(feed.getUser().getId())) {
			throw FeedException.builder()
				.errorCode(FeedErrorCode.FEED_NOT_FOUND)
				.build();
		}

		feedRepository.delete(feed);
		loginUser.decrementFeedCount();
		return this.findAllFeed(LocalDate.now(), LocalDate.now().minusMonths(1), loginUserId,  0, 10);
	}

	/**
	 * [Service] 좋아요 기능 메서드
	 * @param feedId 피드 id
	 * @param loginUserId 로그인 유저 id
	 */
	@Transactional
	public void toggleLike(Long feedId, Long loginUserId){
		Feed feed = feedRepository.findById(feedId).orElseThrow(() -> FeedException
			.builder()
			.errorCode(FeedErrorCode.FEED_NOT_FOUND)
			.build());

		User user = userRepository.getByIdOrThrow(loginUserId);

		// 본인 글에 좋아요를 눌렀을 경우
		if (loginUserId.equals(feed.getUser().getId())) {
			throw FeedException.builder().errorCode(FeedErrorCode.FEED_MY_FEED_NO_LIKES).build();
		}

		Optional<FeedLike> feedLike = feedLikeRepository.findByUserAndFeed(user,feed);

		// 좋아요 내역이 있을 경우
		if (feedLike.isPresent()){
			// FeedLike 데이터 삭제
			FeedLike like = feedLike.get();
			feedLikeRepository.delete(like);
			// Feed 엔티티 좋아요 감소
			feed.decreaseLikes();
		}
		else {
			// Feed 엔티티 좋아요 증가
			feed.increaseLikes();

			// FeedLike 데이터 추가
			feedLikeRepository.save(
				FeedLike.builder()
					.user(user)
					.feed(feed)
					.build()
			);
		}
	}

	/**
	 * 🚀 사용자로 입력받은 날짜 값을 설정하여 반환하는 메서드
	 * @return [0]: 시작날짜, [1]: 마지막날짜
	 */
	public LocalDateTime[] getDefaultDate(LocalDate start, LocalDate end) {
		// 날짜 기본 값이 필요한 경우 (최근 1개월)
		if (start == null || end == null) {
			LocalDate now = LocalDate.now();
			LocalDate monthAgo = now.minusMonths(1);
			return new LocalDateTime[] {
				monthAgo.atStartOfDay(), // 00:00:00
				now.atTime(LocalTime.MAX) // 23:59:59
			};
		}
		// 입력받은 날짜 값의 시간을 설정하여 반환
		else {
			return new LocalDateTime[] {
				start.atStartOfDay(), // 00:00:00
				end.atTime(LocalTime.MAX) // 23:59:59
			};
		}
	}

}
