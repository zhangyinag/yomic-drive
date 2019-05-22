package com.yomic.drive.service;

import com.yomic.drive.domain.User;
import com.yomic.drive.model.UserCreateModel;

import java.util.List;

public interface UserService {
    List<User> getUserList();

    User getUserByUsername (String username);

    Long addUser (User user);

    void deleteUserByUsername (String username);

}
