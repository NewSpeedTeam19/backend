package com.newspeed19.common;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // user는 예약어라서 복수형 추천
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
