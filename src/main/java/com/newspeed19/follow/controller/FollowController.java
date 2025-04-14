package com.newspeed19.follow.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newspeed19.auth.service.JwtProvider;
import com.newspeed19.follow.dto.FollowRequestDto;
import com.newspeed19.follow.dto.FollowResponseDto;
import com.newspeed19.follow.dto.FollowUserResponseDto;
import com.newspeed19.follow.dto.ReceivedFollowResponseDto;
import com.newspeed19.follow.service.FollowService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 팔로우 관련 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

	private final FollowService followService;
	private final JwtProvider jwtProvider;

	/**
	 * 팔로우 요청을 보낸다.
	 */
	@PostMapping
	public FollowResponseDto sendFollowRequest(
		HttpServletRequest request,
		@RequestBody FollowRequestDto dto
	) {
		Long userId = (Long)request.getAttribute("userId");
		return followService.sendFollowRequest(userId, dto.getTargetUserId());
	}

	/**
	 * 보낸 팔로우 요청을 취소한다.
	 */
	@DeleteMapping
	public FollowResponseDto cancelFollowRequest(
		HttpServletRequest request,
		@RequestBody FollowRequestDto dto
	) {
		Long userId = (Long)request.getAttribute("userId");
		return followService.cancelFollowRequest(userId, dto.getTargetUserId());
	}

	/**
	 * 받은 팔로우 요청을 수락한다.
	 */
	@PatchMapping("/accept")
	public FollowResponseDto acceptFollowRequest(
		HttpServletRequest request,
		@RequestBody FollowRequestDto dto
	) {
		Long userId = (Long)request.getAttribute("userId");
		return followService.acceptFollowRequest(userId, dto.getTargetUserId());
	}

	/**
	 * 받은 팔로우 요청을 거절한다.
	 */
	@PatchMapping("/reject")
	public FollowResponseDto rejectFollowRequest(
		HttpServletRequest request,
		@RequestBody FollowRequestDto dto
	) {
		Long userId = (Long)request.getAttribute("userId");
		return followService.rejectFollowRequest(userId, dto.getTargetUserId());
	}

	/**
	 * 받은 팔로우 요청 목록을 조회한다.
	 */
	@GetMapping("/received")
	public List<ReceivedFollowResponseDto> getReceivedFollowRequests(
		HttpServletRequest request,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Long userId = (Long)request.getAttribute("userId");
		return followService.getReceivedFollowRequests(userId, page, size);
	}

	/**
	 * 팔로잉 목록 조회 (공개)
	 */
	@GetMapping("/profile/{userId}/following")
	public List<FollowUserResponseDto> getFollowingList(
		@PathVariable Long userId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return followService.getFollowingList(userId, page, size);
	}

	/**
	 * 팔로워 목록 조회 (공개)
	 */
	@GetMapping("/profile/{userId}/follower")
	public List<FollowUserResponseDto> getFollowerList(
		@PathVariable Long userId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return followService.getFollowerList(userId, page, size);
	}
}
