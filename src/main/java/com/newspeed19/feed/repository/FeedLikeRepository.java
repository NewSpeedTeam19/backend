package com.newspeed19.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newspeed19.feed.entity.Feed;
import com.newspeed19.feed.entity.FeedLike;
import com.newspeed19.user.entity.User;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
	/**
	 * [Repo] 유저가 해당 피드 좋아요를 눌렀는지 여부를 반환하는 메서드
	 * @param user 유저 객체
	 * @param feed 피드 객체
	 * @return 좋아요 눌렀는지 여부를 반환 (true/false)
	 */
	boolean existsByUserAndFeed(User user, Feed feed);

	/**
	 * [Repo] 유저가 피드에 좋아요를 눌렀는지 조회하는 메서드
	 * @param user 유저 객체
	 * @param feed 피드 객체
	 * @return 유저가 피드 누른 결과를 반환
	 */
	Optional<FeedLike> findByUserAndFeed(User user, Feed feed);

	/**
	 * [Repo] 피드의 좋아요 개수를 카운트하는 메서드
	 * @param feedId 피드 id
	 * @return 피드 좋아요 개수를 반환
	 */
	Long countByFeedId(Long feedId);
}
