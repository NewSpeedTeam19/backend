package com.newspeed19.feed.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.newspeed19.feed.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {
	/**
	 * [Repo] userId 목록으로 피드를 조회하는 메서드
	 * @param userIds userId 목록 (ex: 나를 팔로우한 유저 아이디 목록)
	 * @param pageable 페이징 객체
	 * @return 페이징된 피드 객체
	 */
	Page<Feed> findByUserIdIn(List<Long> userIds, Pageable pageable);
}
