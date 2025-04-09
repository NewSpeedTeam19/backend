package com.newspeed19.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newspeed19.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * ID로 사용자를 조회하고 존재하지 않으면 예외를 발생시킵니다.
	 *
	 * @param id 사용자 ID
	 * @return 조회된 사용자
	 * @throws IllegalArgumentException 존재하지 않을 경우
	 */
	default User getByIdOrThrow(Long id) {
		return findById(id)
			.orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다. [id =" + id + "]"));
	}

	Optional<User> findByEmail(String email);

	boolean existsByName(String name);
}
