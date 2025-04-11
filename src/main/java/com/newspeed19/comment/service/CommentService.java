package com.newspeed19.comment.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.newspeed19.comment.dto.request.CommentRequestDto;
import com.newspeed19.comment.dto.response.CommentPageResponseDto;
import com.newspeed19.comment.dto.response.CommentResponseDto;
import com.newspeed19.comment.entity.Comment;
import com.newspeed19.comment.entity.CommentLike;
import com.newspeed19.comment.exception.CommentErrorCode;
import com.newspeed19.comment.exception.CommentException;
import com.newspeed19.comment.repository.CommentLikeRepository;
import com.newspeed19.comment.repository.CommentRepository;
import com.newspeed19.feed.entity.Feed;
import com.newspeed19.feed.exception.FeedErrorCode;
import com.newspeed19.feed.exception.FeedException;
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
	private final CommentLikeRepository commentLikeRepository;

	@Transactional
	public CommentResponseDto create(Long userId, Long feedId, @Valid CommentRequestDto requestDto) {
		User findUser = userRepository.getByIdOrThrow(userId);
		Feed findFeed = feedRepository.findById(feedId)
			.orElseThrow(() -> FeedException
				.builder()
				.errorCode(FeedErrorCode.FEED_NOT_FOUND)
				.build());

		Comment comment = Comment.builder()
			.content(requestDto.getContent())
			.feed(findFeed)
			.user(findUser)
			.build();

		commentRepository.save(comment);
		return new CommentResponseDto(comment, 0L);
	}

	@Transactional(readOnly = true)
	public List<CommentResponseDto> findAll(Long feedId) {
		Feed findFeed = feedRepository.findById(feedId)
			.orElseThrow(() -> FeedException
				.builder()
				.errorCode(FeedErrorCode.FEED_NOT_FOUND)
				.build());
		List<Comment> comments = commentRepository.findByFeedIdOrElseThrow(findFeed);
		return comments.stream()
			.map(comment -> CommentResponseDto.toDto(comment,commentLikeRepository.countByCommentId(comment.getId())))
			.collect(Collectors.toList());

	}

	@Transactional
	public void update(Long id, Long userId, @Valid CommentRequestDto requestDto) {
		Comment comment = commentRepository.findByIdOrElseThrow(id);
		if (!comment.getUser().getId().equals(userId)) {
			throw new CommentException(CommentErrorCode.COMMENT_NOT_ALLOW);
			// throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "작성자만 접근 가능합니다.");
		}
		comment.update(requestDto.getContent());
	}

	@Transactional
	public void delete(Long id, Long userId) {
		Comment comment = commentRepository.findByIdOrElseThrow(id);
		if (!comment.getUser().getId().equals(userId)) {
			throw new CommentException(CommentErrorCode.COMMENT_NOT_ALLOW);
			// throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "작성자만 접근 가능합니다.");
		}
		commentRepository.delete(comment);
	}

	@Transactional(readOnly = true)
	public Page<CommentPageResponseDto> findAllPage(Long feedId, int page, int size) {
		int adjustedPage = (page > 0) ? page - 1 : 0;
		PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("createdAt").ascending());

		Feed findFeed = feedRepository.findById(feedId)
			.orElseThrow(() -> FeedException
				.builder()
				.errorCode(FeedErrorCode.FEED_NOT_FOUND)
				.build());
		Page<Comment> commentPage = commentRepository.findAllByFeedIdOrElseThrow(findFeed, pageable);

		return commentPage.map(comment -> CommentPageResponseDto.builder()
			.id(comment.getId())
			.userId(comment.getUser().getId())
			.feedId(comment.getFeed().getId())
			.content(comment.getContent())
			.countLikes(commentLikeRepository.countByCommentId(comment.getId()))
			.createdAt(comment.getCreatedAt())
			.updatedAt(comment.getUpdatedAt()).build());
	}

	@Transactional
	public void toggleLike(Long commentId, Long userId) {
		Comment comment = commentRepository.findByIdOrElseThrow(commentId);
		User user = userRepository.getByIdOrThrow(userId);

		// 본인 댓글 좋아요 방지
		if(comment.getUser().getId().equals(userId)){
			throw new CommentException(CommentErrorCode.COMMENT_CANT_SELF);
			// throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"본인 댓글에 좋아요 불가능 합니다.");
		}

		//유저와 코멘트로 like 정보 찾기
		Optional<CommentLike> likeStatus = commentLikeRepository.findByUserAndComment(user,comment);

		//현재 댓글의 좋아요 상태 확인
		if(commentLikeRepository.existsByUserAndComment(user,comment)){
			commentLikeRepository.delete(likeStatus.get()); // 좋아요 취소
		}else{ // 좋아요 추가
			commentLikeRepository.save(
				CommentLike.builder()
					.comment(comment)
					.user(user)
					.build()
			);
		}
	}

	public Long countLikes(Long commentId) {
		return commentLikeRepository.countByCommentId(commentId);
	}
}
