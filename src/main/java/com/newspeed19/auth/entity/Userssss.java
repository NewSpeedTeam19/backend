package com.newspeed19.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

/**
 * @packageName    : com.newspeed19.auth.entity
 * @fileName       : User
 * @author         : yong
 * @date           : 4/8/25
 * @description    :
 */
@Entity
@Getter
public class Userssss {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	private String password;

	public Userssss() {
	}

	public Userssss(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
}
