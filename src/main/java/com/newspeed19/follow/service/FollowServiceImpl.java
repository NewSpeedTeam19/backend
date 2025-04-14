package com.newspeed19.follow.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.newspeed19.follow.dto.FollowResponseDto;
import com.newspeed19.follow.dto.FollowUserResponseDto;
import com.newspeed19.follow.dto.ReceivedFollowResponseDto;
import com.newspeed19.follow.entity.Follow;
import com.newspeed19.follow.entity.FollowStatus;
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

	/**
	 * 대상 사용자에게 팔로우 요청을 보냅니다.
	 *
	 * 자신에게 팔로우 요청을 보낼 수 없으며, 이미 PENDING 또는 ACCEPTED 상태의
	 * 요청이 존재할 경우 예외를 발생시킵니다.
	 *
	 * @param fromUserId 팔로우 요청을 보낸 사용자 ID
	 * @param targetUserId 팔로우 요청 대상 사용자 ID
	 * @return 팔로우 요청 결과를 담은 응답 DTO
	 * @throws FollowException 이미 요청이 존재하거나 사용자 정보가 없을 경우 발생
	 */
	@Override
	public FollowResponseDto sendFollowRequest(Long fromUserId, Long targetUserId) {
		if (fromUserId.equals(targetUserId)) {
			throw new FollowException(FollowErrorCode.CANNOT_FOLLOW_SELF);
		}

		User follower = userRepository.getByIdOrThrow(fromUserId);
		User following = userRepository.getByIdOrThrow(targetUserId);

		followRepository.findByFollowerAndFollowing(follower, following).ifPresent(existing -> {
			if (existing.getStatus() == FollowStatus.PENDING || existing.getStatus() == FollowStatus.ACCEPTED) {
				throw new FollowException(FollowErrorCode.FOLLOW_ALREADY_EXISTS);
			}
		});

		Follow follow = Follow.builder()
			.follower(follower)
			.following(following)
			.status(FollowStatus.PENDING)
			.createdAt(LocalDateTime.now())
			.build();

		followRepository.save(follow);

		return FollowResponseDto.builder()
			.message("팔로우가 요청되었습니다.")
			.status(FollowStatus.PENDING)
			.targetUserId(targetUserId)
			.build();
	}

	/**
	 * 사용자가 보낸 팔로우 요청을 취소합니다.
	 *
	 * 취소할 PENDING 상태의 팔로우 요청이 존재하지 않으면 예외를 발생시킵니다.
	 *
	 * @param fromUserId 팔로우 요청을 취소하는 사용자 ID
	 * @param targetUserId 팔로우 요청을 받았던 사용자 ID
	 * @return 팔로우 요청 취소 결과를 담은 응답 DTO
	 * @throws FollowException 취소할 팔로우 요청이 없을 경우 발생
	 */
	@Override
	public FollowResponseDto cancelFollowRequest(Long fromUserId, Long targetUserId) {
		User follower = userRepository.getByIdOrThrow(fromUserId);
		User following = userRepository.getByIdOrThrow(targetUserId);

		Follow follow = followRepository.findByFollowerAndFollowingAndStatus(follower, following, FollowStatus.PENDING)
			.orElseThrow(() -> new FollowException(FollowErrorCode.FOLLOW_CANCEL_NOT_FOUND));

		followRepository.delete(follow);

		return FollowResponseDto.builder()
			.message("팔로우 요청을 취소했습니다.")
			.status(FollowStatus.PENDING)
			.targetUserId(targetUserId)
			.build();
	}

	/**
	 * 받은 팔로우 요청을 수락합니다.
	 *
	 * 수락할 PENDING 상태의 팔로우 요청이 존재하지 않으면 예외를 발생시킵니다.
	 *
	 * @param currentUserId 현재 로그인한 사용자 ID (팔로우 요청을 받은 사람)
	 * @param followerUserId 팔로우 요청을 보낸 사용자 ID
	 * @return 팔로우 요청 수락 결과를 담은 응답 DTO
	 * @throws FollowException 수락할 요청이 없을 경우 발생
	 */
	@Override
	public FollowResponseDto acceptFollowRequest(Long currentUserId, Long followerUserId) {
		User following = userRepository.getByIdOrThrow(currentUserId);
		User follower = userRepository.getByIdOrThrow(followerUserId);

		Follow follow = followRepository.findByFollowerAndFollowingAndStatus(follower, following, FollowStatus.PENDING)
			.orElseThrow(() -> new FollowException(FollowErrorCode.FOLLOW_ACCEPT_NOT_FOUND));

		follow.setStatus(FollowStatus.ACCEPTED);

		follower.incrementFollowing();
		following.incrementFollwer();

		followRepository.save(follow);

		return FollowResponseDto.builder()
			.message("팔로우 요청을 수락했습니다.")
			.status(FollowStatus.ACCEPTED)
			.targetUserId(followerUserId)
			.build();
	}

	/**
	 *받은 팔로우 요청을 거절합니다.
	 *
	 * 거절할 PENDING 상태의 팔로우 요청이 존재하지 않으면 예외를 발생시킵니다.
	 *
	 * @param currentUserId 현재 로그인한 사용자 ID (팔로우 요청을 받은 사람)
	 * @param followerUserId 팔로우 요청을 보낸 사용자 ID
	 * @return 팔로우 요청 결과를 담은 응답 DTO
	 * @throws FollowException 거절할 요청이 없을 경우 발생
	 */
	@Override
	public FollowResponseDto rejectFollowRequest(Long currentUserId, Long followerUserId) {
		User following = userRepository.getByIdOrThrow(currentUserId);
		User follower = userRepository.getByIdOrThrow(followerUserId);

		Follow follow = followRepository.findByFollowerAndFollowingAndStatus(follower, following, FollowStatus.PENDING)
			.orElseThrow(() -> new FollowException(FollowErrorCode.FOLLOW_REJECT_NOT_FOUND));

		follow.setStatus(FollowStatus.REJECTED);

		followRepository.save(follow);

		return FollowResponseDto.builder()
			.message("팔로우 요청을 거절했습니다.")
			.status(FollowStatus.REJECTED)
			.targetUserId(followerUserId)
			.build();
	}

	/**
	 * 현재 사용자가 받은 팔로우 요청 목록을 조회합니다.
	 *
	 * PENDING 상태의 요청만 조회되며, 페이징 처리됩니다.
	 *
	 * @param currentUserId 현재 로그인한 사용자 ID
	 * @param page 페이지 번호 (0부터 시작)
	 * @param size size 페이지 당 항목 수
	 * @return 받은 팔로우 요청 목록
	 */
	@Override
	public List<ReceivedFollowResponseDto> getReceivedFollowRequests(Long currentUserId, int page, int size) {
		User currentUser = userRepository.getByIdOrThrow(currentUserId);

		Page<Follow> receivedRequests = followRepository.findByFollowerAndStatus(
			currentUser,
			FollowStatus.PENDING,
			PageRequest.of(page, size)
		);

		return receivedRequests.stream()
			.map(follow -> {
				User sender = follow.getFollower();
				return ReceivedFollowResponseDto.builder()
					.userId(sender.getId())
					.username(sender.getName())
					.profileImageUrl(sender.getImage())
					.requestedAt(follow.getCreatedAt())
					.build();
			})
			.toList();
	}

	/**
	 * 사용자가 팔로우한(팔로잉) 사용자 목록을 조회합니다.
	 *
	 * ACCEPTED 상태의 팔로우만 포함되며, 페이징 처리됩니다.
	 *
	 * @param userId 대상 사용자 ID
	 * @param page 페이지 번호 (0부터 시작)
	 * @param size 페이지 당 항목 수
	 * @return 팔로잉한 사용자 목록
	 */
	@Override
	public List<FollowUserResponseDto> getFollowingList(Long userId, int page, int size) {
		User follower = userRepository.getByIdOrThrow(userId);

		Page<Follow> followings = followRepository.findByFollowerAndStatus(
			follower,
			FollowStatus.ACCEPTED,
			PageRequest.of(page, size)
		);

		return followings.stream()
			.map(follow -> {
				User following = follow.getFollowing();
				return FollowUserResponseDto.builder()
					.userId(following.getId())
					.username(following.getName())
					.profileImageUrl(following.getImage())
					.build();
			})
			.toList();
	}

	/**
	 * 나를 팔로우한(팔로워) 사용자 목록을 조회합니다.
	 *
	 * ACCEPTED 상태의 팔로우만 포함되며, 페이징 처리합니다.
	 *
	 * @param userId 대상 사용자 ID
	 * @param page 페이지 번호 (0부터 시작)
	 * @param size 페이지 당 항목 수
	 * @return 나를 팔로우한 사용자 목록
	 */
	@Override
	public List<FollowUserResponseDto> getFollowerList(Long userId, int page, int size) {
		User following = userRepository.getByIdOrThrow(userId);

		Page<Follow> followers = followRepository.findByFollowingAndStatus(
			following,
			FollowStatus.ACCEPTED,
			PageRequest.of(page, size)
		);

		return followers.stream()
			.map(follow -> {
				User follower = follow.getFollower();
				return FollowUserResponseDto.builder()
					.userId(follower.getId())
					.username(follower.getName())
					.profileImageUrl(follower.getImage())
					.build();
			})
			.toList();
	}
}
