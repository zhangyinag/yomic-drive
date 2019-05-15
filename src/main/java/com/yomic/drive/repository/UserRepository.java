package com.yomic.drive.repository;

import com.yomic.drive.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
