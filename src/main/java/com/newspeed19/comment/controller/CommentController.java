package com.newspeed19.comment.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.newspeed19.comment.dto.request.CommentRequestDto;
import com.newspeed19.comment.dto.response.CommentPageResponseDto;
import com.newspeed19.comment.dto.response.CommentResponseDto;
import com.newspeed19.comment.service.CommentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	@PostMapping("api/feed/{id}/comments")
	public ResponseEntity<CommentResponseDto> create(
		HttpServletRequest req,
		@PathVariable Long id,
		@Valid @RequestBody CommentRequestDto requestDto
	) {
		long userId = (Long)req.getAttribute("userId");
		return ResponseEntity.ok(commentService.create(userId, id, requestDto));
	}

	@GetMapping("api/feed/{id}/comments")
	public ResponseEntity<List<CommentResponseDto>> findAll(@PathVariable Long id) {
		return ResponseEntity.ok(commentService.findAll(id));
	}

	@PatchMapping("api/comments/{id}")
	public ResponseEntity<String> updateComment(
		HttpServletRequest req,
		@PathVariable Long id,
		@Valid @RequestBody CommentRequestDto requestDto) {
		long userId = (Long)req.getAttribute("userId");
		commentService.update(id, userId, requestDto);
		return ResponseEntity.ok("업데이트 완료");
	}

	@DeleteMapping("api/comments/{id}")
	public ResponseEntity<String> deleteComment(
		HttpServletRequest req,
		@PathVariable Long id) {
		long userId = (Long)req.getAttribute("userId");
		commentService.delete(id, userId);
		return ResponseEntity.ok("삭제 완료");
	}

	@GetMapping("api/feed/{id}/comments/page")
	public ResponseEntity<Page<CommentPageResponseDto>> findAllPage(
		@PathVariable Long id,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Page<CommentPageResponseDto> result = commentService.findAllPage(id, page, size);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/api/comments/{id}/like")
	public ResponseEntity<String> toggleLike(
		HttpServletRequest req,
		@PathVariable Long id
	) {
		long userId = (Long)req.getAttribute("userId");
		commentService.toggleLike(id, userId);
		return ResponseEntity.ok("요청 완료");
	}

}
