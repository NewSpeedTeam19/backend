package com.newspeed19.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newspeed19.feed.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
