package com.newspeed19.feed.dto.response;

import java.util.List;

import com.newspeed19.user.entity.User;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 자세한 피드 정보를 담고 있는 응답 DTO
 */
@Getter
@SuperBuilder
public class FeedDetailResponseDto extends FeedResponseDto {
	private final User user; // FIXME: UserResponseDto 로 변경 예정
	private final List<String> comment; // FIXME: CommentResponseDto 로 변경 예정
}
