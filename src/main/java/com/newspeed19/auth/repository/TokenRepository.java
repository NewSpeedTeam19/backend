package com.newspeed19.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newspeed19.auth.entity.Token;

/**
 * @packageName    : com.newspeed19.auth.repository
 * @fileName       : tokenRepository
 * @author         : yong
 * @date           : 4/9/25
 * @description    :
 */
public interface TokenRepository extends JpaRepository<Token, Long> {
	boolean existsByToken(String token);

	void deleteByToken(String token);
}
