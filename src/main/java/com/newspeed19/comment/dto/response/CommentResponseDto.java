package com.newspeed19.comment.dto.response;

import java.time.LocalDateTime;

import com.newspeed19.comment.entity.Comment;

import lombok.Getter;

@Getter
public class CommentResponseDto {
	private final Long id;
	private final Long userId;
	private final Long feedId;
	private final String content;
	private final Long countLikes;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public CommentResponseDto(Comment comment, Long countLikes) {
		this.id = comment.getId();
		this.userId = comment.getUser().getId();
		this.feedId = comment.getFeed().getId();
		this.content = comment.getContent();
		this.createdAt = comment.getCreatedAt();
		this.updatedAt = comment.getUpdatedAt();
		this.countLikes = countLikes;
	}

	public static CommentResponseDto toDto(Comment comment, Long countLikes) {
		return new CommentResponseDto(comment, countLikes);
	}

}
