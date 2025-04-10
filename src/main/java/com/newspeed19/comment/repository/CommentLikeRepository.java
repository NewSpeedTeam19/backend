package com.newspeed19.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newspeed19.comment.entity.Comment;
import com.newspeed19.comment.entity.CommentLike;
import com.newspeed19.user.entity.User;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
	boolean existsByUserAndComment(User user, Comment comment);
/*	SELECT COUNT(*) > 0
	FROM comment_like
	WHERE user_id = ? AND comment_id = ?*/

	Optional<CommentLike> findByUserAndComment(User user, Comment comment);
	/*SELECT *
	FROM comment_like
	WHERE user_id = ? AND comment_id = ?*/

	long countByCommentId(Long commentId);
	/*SELECT COUNT(*)
	FROM comment_like
	WHERE comment_id = ?*/

	/*Spring Data JPA는
	•	existsBy, findBy, countBy, deleteBy, getBy 이런 접두사로 시작하고
	•	엔티티 필드 이름을 카멜 케이스로 연결해서 메서드 이름을 만들면
	•	자동으로 JPQL → SQL로 번역해서 동작시켜준다고 함*/
}
