package com.newspeed19.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.newspeed19.comment.dto.request.CommentCreateRequestDto;
import com.newspeed19.comment.dto.request.CommentUpdateRequestDto;
import com.newspeed19.comment.dto.response.CommentResponseDto;
import com.newspeed19.comment.entity.Comment;
import com.newspeed19.comment.repository.CommentRepository;
import com.newspeed19.feed.entity.Feed;
import com.newspeed19.feed.repository.FeedRepository;
import com.newspeed19.user.entity.User;
import com.newspeed19.user.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final FeedRepository feedRepository;

	@Transactional
	public CommentResponseDto create(Long userId, Long feedId, @Valid CommentCreateRequestDto requestDto) {
		User findUser = userRepository.findByIdOrElseThrow(userId);
		Feed findFeed = feedRepository.findByIdOrElseThrow(feedId);

		Comment comment = Comment.builder()
			.content(requestDto.getContent())
			.feed(findFeed)
			.user(findUser)
			.build();

		commentRepository.save(comment);
		return new CommentResponseDto(comment);
	}

	public List<CommentResponseDto> findAll(Long feedId) {
		Feed findFeed = feedRepository.findByIdOrElseThrow(feedId);
		List<Comment> comments = commentRepository.findByFeedIdOrElseThrow(findFeed);
		return comments.stream()
			.map(CommentResponseDto::toDto)
			.collect(Collectors.toList());

	}

	@Transactional
	public void update(Long id, Long userId, @Valid CommentUpdateRequestDto requestDto) {
		Comment comment = commentRepository.findByIdOrElseThrow(id);
		if (!comment.getUser().getId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "작성자만 접근 가능합니다.");
		}
		comment.update(requestDto.getContent());
	}

	@Transactional
	public void delete(Long id, Long userId) {
		Comment comment = commentRepository.findByIdOrElseThrow(id);
		if (!comment.getUser().getId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "작성자만 접근 가능합니다.");
		}
		commentRepository.delete(comment);
	}

}
