package com.newspeed19.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newspeed19.comment.entity.Comment;
import com.newspeed19.comment.entity.CommentLike;
import com.newspeed19.feed.entity.Feed;
import com.newspeed19.feed.entity.FeedLike;
import com.newspeed19.user.entity.User;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
	boolean existsByUserAndFeed(User user, Feed feed);
	Optional<FeedLike> findByUserAndFeed(User user, Feed feed);
	long countByFeedId(Long commentId);
}
