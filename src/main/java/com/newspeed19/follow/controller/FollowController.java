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

	@PostMapping("/follow")
	public FollowResponseDto sendFollowRequest(
		@AuthenticationPrincipal
		@RequestBody FollowRequestDto request
	) {
		return followService.sendFollowRequest();
	}

	@DeleteMapping("/follow")
	public FollowResponseDto cancelFollowRequest(
		@AuthenticationPrincipal
		@RequestBody FollowRequestDto request
	) {
		return followService.cancelFollowRequest();
	}

	@PatchMapping("/follow/accept")
	public FollowResponseDto acceptFollowRequest(
		@AuthenticationPrincipal
		@RequestBody FollowRequestDto request
	) {
		return followService.acceptFollowRequest();
	}

	@PatchMapping("/follow/reject")
	public FollowResponseDto rejectFollowRequest(
		@AuthenticationPrincipal
		@RequestBody FollowRequestDto request
	) {
		return followService.rejectFollowRequest();
	}

	@GetMapping("/follow/received")
	public List<ReceivedFollowResponseDto> getReceivedFollowRequests(
		@AuthenticationPrincipal
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return followService.getReceivedFollowRequests();
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
