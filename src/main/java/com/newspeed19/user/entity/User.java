package com.newspeed19.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User Entity
 */

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(nullable = false, unique = true, length = 30)
	private String email;

	@Column(length = 256)
	private String introduction;

	@Column(columnDefinition = "TEXT")
	private String image;

	private Integer age;

	@Column(nullable = false)
	private String password;

	@Column(name = "feed_count")
	private Long feedCount = 0L;

	@Column(name = "follow_count")
	private Long followCount = 0L;

	@Column(name = "following_count")
	private Long followingCount = 0L;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@Builder
	public User(String name, String email, String introduction, String image, Integer age, String password,
		Long followCount, Long followingCount, Boolean isDeleted) {
		this.name = name;
		this.email = email;
		this.introduction = introduction;
		this.image = image;
		this.age = age;
		this.password = password;
		this.followCount = followCount;
		this.followingCount = followingCount;
		this.isDeleted = isDeleted;
	}

	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.followCount = 0L;
		this.followingCount = 0L;
		this.isDeleted = false;
	}

	public void updateProfile(String name, String introduction, String image) {
		if (name != null)
			this.name = name;
		if (introduction != null)
			this.introduction = introduction;
		if (image != null)
			this.image = image;
	}

	public void incrementFeedCount() {
		this.feedCount++;
	}

	public void incrementFollwer() {
		this.followCount++;
	}

	public void incrementFollowing() {
		this.followingCount++;
	}
}
