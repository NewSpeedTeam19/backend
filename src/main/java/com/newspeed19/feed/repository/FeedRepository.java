package com.newspeed19.feed.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.newspeed19.feed.entity.Feed;
import com.newspeed19.user.entity.User;

public interface FeedRepository extends JpaRepository<Feed, Long> {
	/**
	 * [Repo] 기간별로 userId 목록으로 피드를 조회하는 메서드
	 * @param userIds userId 목록 (ex: 나를 팔로우한 유저 아이디 목록)
	 * @param start 시작날짜
	 * @param end 마지막날짜
	 * @param pageable 페이징 객체
	 * @return 페이징된 피드 객체
	 */
	Page<Feed> findByUserIdInAndCreatedAtBetween(List<Long> userIds, LocalDateTime start, LocalDateTime end, Pageable pageable);


	/**
	 * [Repo] 기간별로 작성된 피드를 가지고 오는 메서드
	 * @param start 시작날짜
	 * @param end 마지막날짜
	 * @param pageable 페이징 객체
	 * @return 페이징된 피드 객체
	 */
	Page<Feed> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

	/**
	 * [Repo] 유저가 작성한 피드 개수를 반환하는 메서드
	 * @param userId 유저 id
	 * @return 작성한 피드 개수를 반환
	 */
	Long countByUserId(Long userId);

	Long user(User user);
}