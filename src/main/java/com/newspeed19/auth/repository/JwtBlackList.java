package com.newspeed19.auth.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

/**
 * @packageName    : com.newspeed19.auth.repository
 * @fileName       : JwtBlackList
 * @author         : yong
 * @date           : 4/9/25
 * @description    :
 */
@Repository
public class JwtBlackList {
	public static Map<String, Long> list = new ConcurrentHashMap<>();

	public void add(String token, long expirationTimeMillis) {
		list.put(token, System.currentTimeMillis() + expirationTimeMillis);
	}
	// 로그아웃 //
}
