package com.yomic.drive.service;

import com.yomic.drive.domain.User;
import com.yomic.drive.model.UserCreateModel;

import java.util.List;

public interface UserService {
    List<User> getUserList(Long deptId);

    User getUserByUsername (String username);

    Long addUser (User user);

    void deleteUser (Long id);
}
