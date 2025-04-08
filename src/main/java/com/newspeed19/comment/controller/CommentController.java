package com.newspeed19.comment.controller;

import com.newspeed19.comment.dto.request.CommentCreateRequestDto;
import com.newspeed19.comment.dto.request.CommentUpdateRequestDto;
import com.newspeed19.comment.dto.response.CommentResponseDto;
import com.newspeed19.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("api/feed/{id}/comments")
    public ResponseEntity<CommentResponseDto> create(
            @SessionAttribute(name = "LOGIN_USER") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody CommentCreateRequestDto requestDto
    ){
        return ResponseEntity.ok(commentService.create(userId,id,requestDto));
    }

    @GetMapping("api/feed/{id}/comments")
    public ResponseEntity<List<CommentResponseDto>> findAll(@PathVariable Long id){
        return ResponseEntity.ok(commentService.findAll(id));
    }

    @PatchMapping("api/comments/{id}")
    public ResponseEntity<String> updateComment(
            @SessionAttribute(name = "LOGIN_USER") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequestDto requestDto){
        commentService.update(id, userId, requestDto);
        return ResponseEntity.ok("업데이트 완료");
    }

    @DeleteMapping("api/comments/{id}")
    public ResponseEntity<String> deleteComment(
            @SessionAttribute(name = "LOGIN_USER") Long userId,
            @PathVariable Long id){
        commentService.delete(id, userId);
        return ResponseEntity.ok("삭제 완료");
    }

}
