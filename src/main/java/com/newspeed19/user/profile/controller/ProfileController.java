package com.newspeed19.user.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.newspeed19.user.entity.User;
import com.newspeed19.user.profile.dto.MessageResponseDto;
import com.newspeed19.user.profile.dto.UserProfileResponseDto;
import com.newspeed19.user.profile.dto.UserProfileUpdateRequestDto;
import com.newspeed19.user.profile.service.ProfileService;
import com.newspeed19.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;
	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;

	// 내 프로필 조회
	// 내 프로필 조회
	@GetMapping
	public ResponseEntity<UserProfileResponseDto> getMyProfile(HttpServletRequest request) {
		Long userId = (Long)request.getAttribute("userId");

		User user = userRepository.getByIdOrThrow(userId);
		return ResponseEntity.ok(profileService.getMyProfile(user));
	}

	// 내 프로필 수정
	@PutMapping
	public ResponseEntity<MessageResponseDto> updateProfile(HttpServletRequest request,
		@RequestBody @Valid UserProfileUpdateRequestDto req) {
		Long userId = Long.valueOf((String)request.getAttribute("userId"));
		User user = userRepository.getByIdOrThrow(userId);
		profileService.updateMyProfile(user, requestDto);
		return ResponseEntity.ok(new MessageResponseDto("프로필이 성공적으로 수정되었습니다."));
	}

	// 타인 프로필 조회
	@GetMapping("/{id}")
	public ResponseEntity<UserProfileResponseDto> getOtherUserProfile(@PathVariable Long id) {
		return ResponseEntity.ok(profileService.getUserProfile(id));
	}
}
