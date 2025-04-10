package com.newspeed19.feed.dto.response;

import java.util.List;

import com.newspeed19.comment.dto.response.CommentResponseDto;
import com.newspeed19.user.profile.dto.UserProfileResponseDto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 자세한 피드 정보를 담고 있는 응답 DTO
 */
@Getter
@SuperBuilder
public class FeedDetailResponseDto extends FeedResponseDto {
	private final UserProfileResponseDto user;
	private final List<CommentResponseDto> comments;
}
