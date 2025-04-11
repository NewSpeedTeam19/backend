package com.newspeed19.feed.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newspeed19.feed.dto.request.FeedRequestDto;
import com.newspeed19.feed.dto.request.FeedRequestGroups;
import com.newspeed19.feed.dto.response.ApiResponseDto;
import com.newspeed19.feed.dto.response.FeedDetailResponseDto;
import com.newspeed19.feed.dto.response.FeedPageResponseDto;
import com.newspeed19.feed.service.FeedService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {
	private final FeedService feedService;

	/**
	 * [Controller] 전체 피드를 조회하는 메서드
	 * @param startDate 기간검색 시, 시작날짜 (기본 값: 1달 전 00:00:00)
	 * @param endDate 기간 검색 시, 마지막 날짜 (기본 값: 오늘 23:59:59)
	 * @param request HttpServletRequest 객체
	 * @param page 페이지 번호
	 * @param size 페이지 사이즈
	 * @return 페이징 피드정보가 포함된 응답객체를 반환
	 */
	@GetMapping
	public ResponseEntity<ApiResponseDto<FeedPageResponseDto>> findAll(
		@RequestParam(value = "startDate", required = false)
		@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
		@RequestParam(value = "endDate", required = false)
		@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		HttpServletRequest request
	) {
		FeedPageResponseDto responseDto = feedService
			.findAllFeed(startDate, endDate, getLoginUserId(request), page, size);

		ApiResponseDto<FeedPageResponseDto> apiResponseDto = ApiResponseDto.<FeedPageResponseDto>builder()
			.code(HttpStatus.OK.value())
			.message("전체 피드를 조회합니다.")
			.status(HttpStatus.OK.getReasonPhrase())
			.data(responseDto)
			.build();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
	}

	/**
	 * [Controller] 단일 피드를 조회하는 메서드
	 * @param id 피드 id
	 * @return 단일 피드 정보가 포함된 응답객체를 반환
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDto<FeedDetailResponseDto>> findById(
		@PathVariable Long id
	) {
		FeedDetailResponseDto responseDto = feedService.findFeedById(id);

		ApiResponseDto<FeedDetailResponseDto> apiResponseDto = ApiResponseDto.<FeedDetailResponseDto>builder()
			.code(HttpStatus.OK.value())
			.message("상세 피드를 조회합니다.")
			.status(HttpStatus.OK.getReasonPhrase())
			.data(responseDto)
			.build();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
	}

	/**
	 * [Controller] 피드를 생성하는 메서드
	 * @param dto 사용자 요청 DTO
	 * @param request HttpServletRequest 객체
	 * @return 생성된 피드 정보가 포함된 응답객체를 반환
	 */
	@PostMapping("/create")
	public ResponseEntity<ApiResponseDto<FeedDetailResponseDto>> create(
		@Validated(FeedRequestGroups.Create.class) @RequestBody FeedRequestDto dto,
		HttpServletRequest request
	) {
		FeedDetailResponseDto responseDto = feedService.createFeed(getLoginUserId(request), dto);

		ApiResponseDto<FeedDetailResponseDto> apiResponseDto = ApiResponseDto.<FeedDetailResponseDto>builder()
			.code(HttpStatus.CREATED.value())
			.message("피드를 생성하였습니다.")
			.status(HttpStatus.CREATED.getReasonPhrase())
			.data(responseDto)
			.build();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.CREATED);
	}

	/**
	 * [Controller] 피드를 수정하는 메서드
	 * @param id 피드 id
	 * @param dto 사용자 요청 DTO
	 * @param request HttpServletRequest 객체
	 * @return 수정된 피드 정보가 포함된 응답객체를 반환
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponseDto<FeedDetailResponseDto>> update(
		@PathVariable Long id,
		@Validated(FeedRequestGroups.Update.class) @RequestBody FeedRequestDto dto,
		HttpServletRequest request
	) {
		FeedDetailResponseDto responseDto = feedService.updateFeed(dto, getLoginUserId(request), id);

		ApiResponseDto<FeedDetailResponseDto> apiResponseDto = ApiResponseDto.<FeedDetailResponseDto>builder()
			.code(HttpStatus.OK.value())
			.message("피드를 성공적으로 수정하였습니다.")
			.status(HttpStatus.OK.getReasonPhrase())
			.data(responseDto)
			.build();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
	}

	/**
	 * [Controller] 단일 피드를 삭제하는 메서드
	 * @param id 피드 id
	 * @param dto 사용자 요청 DTO
	 * @param request HttpServletRequest 객체
	 * @return 업데이트된 페이징 피드 정보가 포함된 응답객체를 반환
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDto<FeedPageResponseDto>> delete(
		@PathVariable Long id,
		@Validated(FeedRequestGroups.Delete.class) @RequestBody FeedRequestDto dto,
		HttpServletRequest request
	) {
		FeedPageResponseDto responseDto = feedService.deleteFeed(getLoginUserId(request), id);

		ApiResponseDto<FeedPageResponseDto> apiResponseDto = ApiResponseDto.<FeedPageResponseDto>builder()
			.code(HttpStatus.OK.value())
			.message("성공적으로 삭제하였습니다. 전체 피드를 반환합니다.")
			.status(HttpStatus.OK.getReasonPhrase())
			.data(responseDto)
			.build();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
	}

	/**
	 * [Controller] 피드 좋아요 기능을 처리하는 메서드
	 * @param request HttpServletRequest 객체
	 * @param id 피드 id
	 * @return
	 */
	@PostMapping("/{id}/like")
	public ResponseEntity<ApiResponseDto<Void>> toggleLike(
		HttpServletRequest request,
		@PathVariable Long id
	) {
		feedService.toggleLike(id, getLoginUserId(request));

		ApiResponseDto<Void> apiResponseDto = ApiResponseDto.<Void>builder()
			.code(HttpStatus.OK.value())
			.message("좋아요 요청을 성공적으로 처리하였습니다.")
			.status(HttpStatus.OK.getReasonPhrase())
			.build();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
	}

	/**
	 * 🚀 세션에 저장되어 있는 로그인 유저 아이디 가져오는 메서드
	 * @param request HttpServletRequest 객체
	 * @return Long 타입의 로그인 유저 아이디 반환
	 */
	private Long getLoginUserId(HttpServletRequest request) {
		return (Long)request.getAttribute("userId");
	}
}
