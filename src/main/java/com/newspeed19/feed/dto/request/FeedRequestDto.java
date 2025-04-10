package com.newspeed19.feed.dto.request;

import static com.newspeed19.feed.dto.request.FeedRequestGroups.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 피드관련 사용자의 입력 데이터를 담고있는 요청 DTO
 */
@JsonIgnoreProperties // 존재하지 않은 필드 들어오면 예외 발생
@Getter
@RequiredArgsConstructor
public class FeedRequestDto {
	@NotBlank(message = "내용을 입력해주세요.", groups = {Create.class, Update.class})
	@Size(max = 500, message = "내용은 500자 이내로 입력해주세요.", groups = {Create.class, Update.class})
	private final String contents;

	@NotBlank(message = "이미지를 첨부해주세요.", groups = {Create.class, Update.class})
	private final String image;
}
