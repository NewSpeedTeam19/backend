package com.newspeed19.profile.repository;

import com.newspeed19.common.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
