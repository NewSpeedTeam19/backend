package com.newspeed19.comment.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentPageResponseDto {
	private final Long id;
	private final Long userId;
	private final Long feedId;
	private final String content;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	@Builder
	public CommentPageResponseDto(Long id, Long userId, Long feedId, String content, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.userId = userId;
		this.feedId = feedId;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
