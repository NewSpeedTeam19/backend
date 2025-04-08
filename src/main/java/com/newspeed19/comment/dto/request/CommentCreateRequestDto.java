package com.newspeed19.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
