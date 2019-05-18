package com.yomic.drive.service;

import com.yomic.drive.domain.User;
import com.yomic.drive.model.UserModel;

import java.util.List;

public interface UserService {
    List<User> getUserList();

    User getUserByUsername (String username);

    User addUser (UserModel userModel);

    void deleteUserByUsername (String username);
}
