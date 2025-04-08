package com.newspeed19.follow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.newspeed19.follow.dto.FollowResponseDto;
import com.newspeed19.follow.dto.FollowUserDto;
import com.newspeed19.follow.dto.ReceivedFollowRequestDto;
import com.newspeed19.follow.exception.FollowErrorCode;
import com.newspeed19.follow.exception.FollowException;
import com.newspeed19.follow.repository.FollowRepository;
import com.newspeed19.user.entity.User;
import com.newspeed19.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	@Override
	public FollowResponseDto sendFollowRequest(Long fromUserId, Long targetUserId) {
		if (fromUserId.equals(targetUserId)) {
			throw new FollowException(FollowErrorCode.FOLLOW_ALREADY_EXISTS, "자기 자신에게는 팔로우 요청을 보낼 수 없습니다.");
		}

		User follower = userRepository.findById(fromUserId)
			.orElseThrow()
	}

	@Override
	public FollowResponseDto cancelFollowRequest(Long fromUserId, Long targetUserId) {
		return null;
	}

	@Override
	public FollowResponseDto acceptFollowRequest(Long currentUserId, Long followerUserId) {
		return null;
	}

	@Override
	public FollowResponseDto rejectFollowRequest(Long currentUserId, Long followerUserId) {
		return null;
	}

	@Override
	public List<ReceivedFollowRequestDto> getReceivedFollowRequests(Long currentUserId, int page, int size) {
		return List.of();
	}

	@Override
	public List<FollowUserDto> getFollowingList(Long userId, int page, int size) {
		return List.of();
	}

	@Override
	public List<FollowUserDto> getFollowerList(Long userId, int page, int size) {
		return List.of();
	}
}
