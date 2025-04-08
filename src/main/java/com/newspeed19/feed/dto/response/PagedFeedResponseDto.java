package com.newspeed19.feed.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

/**
 *  피드 목록과 페이징 정보를 담고있는 응답 DTO
 */
@Getter
@Builder
public class PagedFeedResponseDto {
	private final PageResponseDto pages;
	private final List<FeedResponseDto> feeds;
}
