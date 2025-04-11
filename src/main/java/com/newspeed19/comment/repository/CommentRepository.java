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
			.errorCode(CommentErrorCode.COMMENT_NOT_ALLOW)
			.build());
		// return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."));
	}

	List<Comment> findAllByFeedId(Long feedId);

	default List<Comment> findByFeedIdOrElseThrow(Feed feed) {
		List<Comment> comments = findAllByFeedId(feed.getId());
		if (comments.isEmpty()) {
			throw new CommentException(CommentErrorCode.COMMENT_NOT_FOUN);
			// throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 없습니다.");
		}
		return comments;
	}

	Page<Comment> findAllByFeedId(Long feedId, Pageable pageable);

	default Page<Comment> findAllByFeedIdOrElseThrow(Feed feed, Pageable pageable) {
		Page<Comment> pageComment = findAllByFeedId(feed.getId(), pageable);

		if (pageComment.isEmpty()) {
			throw new CommentException(CommentErrorCode.COMMENT_NOT_FOUN);
			// throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 없습니다.");
		}

		return pageComment;
	}

	Long countByFeedId(Long feedId);
}
