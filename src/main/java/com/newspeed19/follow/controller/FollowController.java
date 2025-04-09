package com.newspeed19.follow.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.newspeed19.follow.dto.FollowRequestDto;
import com.newspeed19.follow.dto.FollowResponseDto;
import com.newspeed19.follow.dto.FollowUserResponseDto;
import com.newspeed19.follow.dto.ReceivedFollowResponseDto;
import com.newspeed19.follow.service.FollowService;

import lombok.RequiredArgsConstructor;

/**
 * 팔로우 관련 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FollowController {

	private final FollowService followService;

	private long getUserIdFromToken(String authorization) {
		String token = authorization.replace("Bearer ", "");
		DecodedJWT decodedJWT = JWT.decode(token);
		return Long.valueOf(decodedJWT.getSubject());
	}

	@PostMapping("/follow")
	public FollowResponseDto sendFollowRequest(
		@RequestHeader("Authorization") String authorization,
		@RequestBody FollowRequestDto request
	) {
		Long userId = getUserIdFromToken(authorization);
		return followService.sendFollowRequest(userId, request.getTargetUserId());
	}

	@DeleteMapping("/follow")
	public FollowResponseDto cancelFollowRequest(
		@RequestHeader("Authorization") String authorization,
		@RequestBody FollowRequestDto request
	) {
		Long userId = getUserIdFromToken(authorization);
		return followService.cancelFollowRequest(userId, request.getTargetUserId());
	}

	@PatchMapping("/follow/accept")
	public FollowResponseDto acceptFollowRequest(
		@RequestHeader("Authorization") String authorization,
		@RequestBody FollowRequestDto request
	) {
		Long userId = getUserIdFromToken(authorization);
		return followService.acceptFollowRequest(userId, request.getTargetUserId());
	}

	@PatchMapping("/follow/reject")
	public FollowResponseDto rejectFollowRequest(
		@RequestHeader("Authorization") String authorization,
		@RequestBody FollowRequestDto request
	) {
		Long userId = getUserIdFromToken(authorization);
		return followService.rejectFollowRequest(userId, request.getTargetUserId());
	}

	@GetMapping("/follow/received")
	public List<ReceivedFollowResponseDto> getReceivedFollowRequests(
		@RequestHeader("Authorization") String authorization,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Long userId = getUserIdFromToken(authorization);
		return followService.getReceivedFollowRequests(userId, page, size);
	}

	@GetMapping("/profile/{userId}/following")
	public List<FollowUserResponseDto> getFollowingList(
		@PathVariable Long userId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return followService.getFollowingList(userId, page, size);
	}

	@GetMapping("/profile/{userId}/follower")
	public List<FollowUserResponseDto> getFollowerList(
		@PathVariable Long userId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return followService.getFollowerList(userId, page, size);
	}
}
