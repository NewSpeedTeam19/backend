package com.newspeed19.feed.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.newspeed19.feed.dto.request.FeedRequestDto;
import com.newspeed19.feed.dto.response.FeedDetailResponseDto;
import com.newspeed19.feed.dto.response.FeedResponseDto;
import com.newspeed19.feed.dto.response.PageResponseDto;
import com.newspeed19.feed.dto.response.PagedFeedResponseDto;
import com.newspeed19.feed.entity.Feed;
import com.newspeed19.feed.repository.FeedRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {

	private final FeedRepository feedRepository;

	/**
	 * [Service] 전체 피드를 조회하는 메서드
	 * @param pageable 페이징 객체
	 * @return 페이징된 피드 응답객체를 반환
	 */
	@Transactional(readOnly = true)
	public PagedFeedResponseDto findAllFeed(Pageable pageable) {
		Page<FeedResponseDto> pagedFeedResponseDto = feedRepository.findAll(pageable)
			.map(feed ->
				// FIXME: 댓글 불러오기 로직 추가 예정
				FeedResponseDto.builder()
					.id(feed.getId())
					.contents(feed.getContents())
					.image(feed.getImage())
					.createdAt(feed.getCreatedAt())
					.updatedAt(feed.getUpdatedAt())
					.build()
			);
		// 응답 객체 생성
		List<FeedResponseDto> feeds = pagedFeedResponseDto.getContent();
		PageResponseDto pages = PageResponseDto.from(pagedFeedResponseDto);

		return PagedFeedResponseDto.builder()
			.pages(pages)
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
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."));

		return FeedDetailResponseDto.builder()
			.id(feed.getId())
			.contents(feed.getContents())
			.image(feed.getImage())
			.createdAt(feed.getCreatedAt())
			.updatedAt(feed.getUpdatedAt())
			.user(feed.getUser())
			.comment(List.of("좋아요!", "응원합니다")) // FIXME: comments 응답 객체
			.build();
	}

	/**
	 * [Service] 피드를 생성하는 메서드
	 * @param dto 사용자 요청 DTO
	 * @return 생성된 상세피드 응답객체를 반환
	 */
	@Transactional
	public FeedDetailResponseDto createFeed(FeedRequestDto dto) {
		// FIXME: 비밀번호 검증 로직 필요

		// FIXME: User 저장해야 함
		// 피드 생성
		Feed feed = feedRepository.save(
			Feed.builder()
				.contents(dto.getContents())
				.image(dto.getImage())
				.build()
		);

		return FeedDetailResponseDto.builder()
			.id(feed.getId())
			.contents(feed.getContents())
			.image(feed.getImage())
			.createdAt(feed.getCreatedAt())
			.updatedAt(feed.getUpdatedAt())
			.user(feed.getUser())
			.comment(List.of("좋아요!", "응원합니다")) // FIXME: comments 응답 객체
			.build();
	}

	/**
	 * [Service] 피드를 수정하는 메서드
	 * @param dto 사용자 요청 DTO
	 * @param id 피드 id
	 * @return 수정된 상세피드 응답객체를 반환
	 */
	@Transactional
	public FeedDetailResponseDto updateFeed(FeedRequestDto dto, Long id) {
		// FIXME: 비밀번호 검증 로직 필요
		Feed feed = feedRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."));

		// Feed 업데이트
		feed.updateFeed(dto.getImage(), dto.getContents());

		return FeedDetailResponseDto.builder()
			.id(feed.getId())
			.contents(feed.getContents())
			.image(feed.getImage())
			.createdAt(feed.getCreatedAt())
			.updatedAt(feed.getUpdatedAt())
			.user(feed.getUser())
			.comment(List.of("좋아요!", "응원합니다")) // FIXME: comments 응답 객체
			.build();
	}

	/**
	 * [Service] 피드를 삭제하는 메서드
	 * @param id 피드 Id
	 * @param password 유저 비밀번호
	 * @return 업데이트된 페이징 피드 응답객체를 반환
	 */
	@Transactional
	public PagedFeedResponseDto deleteFeed(Long id, String password) {
		// FIXME: 비밀번호 검증 로직 필요
		// FIXME: 로그인 유저 권한여부 로직 필요

		Feed feed = feedRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."));

		feedRepository.delete(feed);

		return this.findAllFeed(getDefaultPageable());
	}

	/**
	 * 🚀 기본 페이징 객체를 반환하는 메서드 (생성일 내림차순)
	 * @return 페이징 객체를 반환
	 */
	private Pageable getDefaultPageable() {
		return PageRequest.of(
			0,
			10,
			Sort.by(Sort.Direction.DESC, "createdAt"));
	}
}
