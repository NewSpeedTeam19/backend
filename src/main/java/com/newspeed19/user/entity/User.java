package com.newspeed19.user.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
