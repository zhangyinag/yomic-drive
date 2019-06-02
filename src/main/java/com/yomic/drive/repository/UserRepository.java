package com.yomic.drive.repository;

import com.yomic.drive.domain.Dept;
import com.yomic.drive.domain.User;
import com.yomic.drive.repository.common.BaseRepository;

import java.util.List;

public interface UserRepository extends BaseRepository<User> {
    User findUserByUsername(String username);

    List<User> findUsersByDeptIsNull();

    List<User> findUsersByDept(Dept dept);
}
