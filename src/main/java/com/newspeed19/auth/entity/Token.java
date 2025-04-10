package com.newspeed19.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

/**
 * @packageName    : com.newspeed19.auth.entity
 * @fileName       : Token
 * @author         : yong
 * @date           : 4/10/25
 * @description    :
 */
@Entity
@NoArgsConstructor
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	public Token(String token) {
		this.token = token;
	}
}
