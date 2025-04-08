package com.newspeed19.feed.dto.request;

/**
 * FeedRequestDto, FeedController 에서 사용되는 유효성 검증 그룹 클래스
 * - FeedRequestDto: 요청 타입을 groups 에 지정하여 검증할 필드를 구분함
 * - FeedController: @Validated 어노테이션과 함께 검증할 필드를 구분함
 */
public class FeedRequestGroups {
	public interface Create {
	}

	public interface Update {
	}

	public interface Delete {
	}
}
