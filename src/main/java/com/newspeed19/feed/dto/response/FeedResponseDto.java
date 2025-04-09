package com.newspeed19.feed.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 기본적인 피드정보를 담고있는 응답 DTO
 */
@Getter
@SuperBuilder
public class FeedResponseDto {
	private final Long id;

	private final String contents;

	private final String image;

	@Builder.Default
	private Long likes = 0L;

	@Builder.Default
	private Long comments = 0L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime updatedAt;
}
