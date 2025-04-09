package com.newspeed19.feed.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Getter;

/**
 *  피드 목록과 페이징 정보를 담고있는 응답 DTO
 */
@Getter
@Builder
@JsonPropertyOrder({"feeds", "pages"})
public class FeedPageResponseDto {
	private final List<FeedResponseDto> feeds;
	private final Page<FeedResponseDto> pages;
}
