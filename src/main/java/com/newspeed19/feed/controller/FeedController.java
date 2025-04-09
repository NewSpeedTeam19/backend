package com.newspeed19.feed.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import com.newspeed19.feed.dto.response.PagedFeedResponseDto;
import com.newspeed19.feed.service.FeedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {
	private final FeedService feedService;

	/**
	 * [Controller] 전체 피드를 조회하는 메서드
	 * @param page 페이지 번호
	 * @param size 페이지 사이즈
	 * @return 페이징 피드정보가 포함된 응답객체를 반환
	 */
	@GetMapping
	public ResponseEntity<ApiResponseDto<FeedPageResponseDto>> findAll(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		FeedPageResponseDto responseDto = feedService.findAllFeed(page, size);

		ApiResponseDto<PagedFeedResponseDto> apiResponseDto = ApiResponseDto.<PagedFeedResponseDto>builder()
			.success(true)
			.message("전체 피드를 조회합니다.")
			.httpStatus(HttpStatus.OK)
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
			.success(true)
			.message("상세 피드를 조회합니다.")
			.httpStatus(HttpStatus.OK)
			.data(responseDto)
			.build();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
	}

	/**
	 * [Controller] 피드를 생성하는 메서드
	 * @param dto 사용자 요청 DTO
	 * @return 생성된 피드 정보가 포함된 응답객체를 반환
	 */
	@PostMapping("/create")
	public ResponseEntity<ApiResponseDto<FeedDetailResponseDto>> create(
		@Validated(FeedRequestGroups.Create.class) @RequestBody FeedRequestDto dto
	) {
		FeedDetailResponseDto responseDto = feedService.createFeed(dto);

		ApiResponseDto<FeedDetailResponseDto> apiResponseDto = ApiResponseDto.<FeedDetailResponseDto>builder()
			.success(true)
			.message("피드를 생성하였습니다.")
			.httpStatus(HttpStatus.CREATED)
			.data(responseDto)
			.build();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.CREATED);
	}

	/**
	 * [Controller] 피드를 수정하는 메서드
	 * @param id 피드 id
	 * @param dto 사용자 요청 DTO
	 * @return 수정된 피드 정보가 포함된 응답객체를 반환
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponseDto<FeedDetailResponseDto>> update(
		@PathVariable Long id,
		@Validated(FeedRequestGroups.Update.class) @RequestBody FeedRequestDto dto
	) {
		FeedDetailResponseDto responseDto = feedService.updateFeed(dto, id);

		ApiResponseDto<FeedDetailResponseDto> apiResponseDto = ApiResponseDto.<FeedDetailResponseDto>builder()
			.success(true)
			.message("피드를 성공적으로 수정하였습니다.")
			.httpStatus(HttpStatus.OK)
			.data(responseDto)
			.build();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
	}

	/**
	 * [Controller] 단일 피드를 삭제하는 메서드
	 * @param id 피드 id
	 * @param dto 사용자 요청 DTO
	 * @return 업데이트된 페이징 피드 정보가 포함된 응답객체를 반환
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDto<PagedFeedResponseDto>> delete(
		@PathVariable Long id,
		@Validated(FeedRequestGroups.Delete.class) @RequestBody FeedRequestDto dto
	) {
		PagedFeedResponseDto responseDto = feedService.deleteFeed(id, dto.getPassword());

		ApiResponseDto<PagedFeedResponseDto> apiResponseDto = ApiResponseDto.<PagedFeedResponseDto>builder()
			.success(true)
			.message("성공적으로 삭제하였습니다. 전체 피드를 반환합니다.")
			.httpStatus(HttpStatus.OK)
			.data(responseDto)
			.build();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
	}
}
