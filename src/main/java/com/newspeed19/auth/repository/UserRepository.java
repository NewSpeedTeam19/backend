package com.newspeed19.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newspeed19.auth.entity.User;

/**
 * @packageName    : com.newspeed19.auth.authRepository
 * @fileName       : UserRepoi
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	boolean existsByName(String name);
}
