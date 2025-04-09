package com.newspeed19.feed.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.newspeed19.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feed")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // FIXME: BaseEntity 상속 시, 제거
public class Feed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "longtext")
	private String contents;

	@Column(nullable = false, columnDefinition = "text")
	private String image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id") // FIXME: nullable = false 붙일 예정
	@Setter
	private User user;

	// FIXME: BaseEntity 상속 시, 제거
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	/**
	 * ✅ Builder 생성자
	 * @param contents 피드 내용
	 * @param image 피드 이미지 url
	 * @param user 피드 생성한 유저
	 */
	@Builder
	public Feed(String contents, String image, User user) {
		this.contents = contents;
		this.image = image;
		this.user = user;
	}

	/**
	 * 🚀 피드 업데이트 메서드
	 * @param contents 피드 내용
	 * @param image 피드 이미지 url
	 */
	public void updateFeed(String contents, String image) {
		this.contents = contents;
		this.image = image;
	}
}
