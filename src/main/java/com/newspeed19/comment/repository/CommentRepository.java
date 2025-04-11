package com.newspeed19.comment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.newspeed19.comment.entity.Comment;
import com.newspeed19.comment.exception.CommentErrorCode;
import com.newspeed19.comment.exception.CommentException;
import com.newspeed19.feed.entity.Feed;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	default Comment findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(()->CommentException.builder()
			.errorCode(CommentErrorCode.COMMENT_NOT_FOUN)
			.build());
	}

	List<Comment> findAllByFeedId(Long feedId);

	default List<Comment> findByFeedIdOrElseThrow(Feed feed) {
		List<Comment> comments = findAllByFeedId(feed.getId());
		if (comments.isEmpty()) {
			throw new CommentException(CommentErrorCode.COMMENT_NOT_FOUN);
		}
		return comments;
	}

	Page<Comment> findAllByFeedId(Long feedId, Pageable pageable);

	default Page<Comment> findAllByFeedIdOrElseThrow(Feed feed, Pageable pageable) {
		Page<Comment> pageComment = findAllByFeedId(feed.getId(), pageable);

		if (pageComment.isEmpty()) {
			throw new CommentException(CommentErrorCode.COMMENT_NOT_FOUN);
		}

		return pageComment;
	}

	Long countByFeedId(Long feedId);
}
