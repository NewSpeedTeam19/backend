package com.newspeed19.feed.dto.response;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Getter;

/**
 * 페이징 정보를 담고있는 응답 DTO
 */
@Getter
@Builder
public class PageResponseDto {
	private final int totalCount; // 전체 항목 수
	private final int totalPages; // 전체 페이지 개수
	private final int currentPage; // 현재 페이지 번호
	private final int size; // 한 페이지당 보여주는 항목 수

	private final boolean hasNext; // 다음 페이지 존재여부
	private final boolean hasPrevious; // 이전 페이지 존재여부
	private final boolean isLast; // 마지막 페이지 여부

	/**
	 * 🚀 다양한 Page 객체를 갖고 PageResponseDto 생성하는 메서드
	 * @param page Page 객체
	 * @return PageResponseDto 빌더로 생성하여 반환
	 */
	public static PageResponseDto from(Page<?> page) {
		return PageResponseDto.builder()
			.totalCount((int)page.getTotalElements())
			.totalPages(page.getTotalPages())
			.currentPage(page.getNumber())
			.size(page.getSize())
			.hasNext(page.hasNext())
			.hasPrevious(page.hasPrevious())
			.isLast(page.isLast())
			.build();
	}
}
