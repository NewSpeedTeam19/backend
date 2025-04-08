package com.newspeed19.follow.entity;

import java.time.LocalDateTime;

import com.newspeed19.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 팔로우 관계를 나타내는 엔티티
 */
@Entity
@Table(name = "follows")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follower_id", nullable = false)
	private User follower;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following_id", nullable = false)
	private User following;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FollowStatus status;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
}
