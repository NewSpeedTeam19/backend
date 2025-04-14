package com.newspeed19.follow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.newspeed19.follow.entity.Follow;
import com.newspeed19.follow.entity.FollowStatus;
import com.newspeed19.user.entity.User;

/**
 * 팔로우 관련 DB 접근을 처리하는 레포지토리
 */
public interface FollowRepository extends JpaRepository<Follow, Long> {

	Optional<Follow> findByFollowerAndFollowing(User follower, User following);

	Optional<Follow> findByFollowerAndFollowingAndStatus(User follower, User following, FollowStatus status);

	List<Follow> findByFollowingAndStatus(User following, FollowStatus status);

	Page<Follow> findByFollowerAndStatus(User follower, FollowStatus status, Pageable pageable);

	Page<Follow> findByFollowingAndStatus(User following, FollowStatus status, Pageable pageable);

	@Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId AND f.status = 'ACCEPTED'")
	List<Long> findByFollowingUserIds(@Param("userId") Long userId);
}
