package com.newspeed19.follow.service;

import java.util.List;

import com.newspeed19.follow.dto.FollowResponseDto;
import com.newspeed19.follow.dto.FollowUserResponseDto;
import com.newspeed19.follow.dto.ReceivedFollowResponseDto;

/**
 * 팔로우 기능의 비즈니스 로직 인터페이스
 */
public interface FollowService {

	FollowResponseDto sendFollowRequest(Long fromUserId, Long targetUserId);

	FollowResponseDto cancelFollowRequest(Long fromUserId, Long targetUserId);

	FollowResponseDto acceptFollowRequest(Long currentUserId, Long followerUserId);

	FollowResponseDto rejectFollowRequest(Long currentUserId, Long followerUserId);

	List<ReceivedFollowResponseDto> getReceivedFollowRequests(Long currentUserId, int page, int size);

	List<FollowUserResponseDto> getFollowingList(Long userId, int page, int size);

	List<FollowUserResponseDto> getFollowerList(Long userId, int page, int size);
}
