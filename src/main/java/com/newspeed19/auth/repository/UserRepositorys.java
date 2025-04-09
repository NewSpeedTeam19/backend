package com.newspeed19.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newspeed19.auth.entity.Userssss;

/**
 * @packageName    : com.newspeed19.auth.authRepository
 * @fileName       : UserRepoi
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */
public interface UserRepositorys extends JpaRepository<Userssss, Long> {
	Optional<Userssss> findByEmail(String email);

	boolean existsByName(String name);
}
