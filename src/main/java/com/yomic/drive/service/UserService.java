package com.yomic.drive.service;

import com.yomic.drive.domain.User;

import java.util.List;

public interface UserService {
    List<User> getUserList();

    User getUserByUsername (String username);

    User addUser (User user);

    void deleteUserByUsername (String username);
}
