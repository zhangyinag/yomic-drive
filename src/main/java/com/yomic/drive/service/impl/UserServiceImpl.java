package com.yomic.drive.service.impl;

import com.yomic.drive.domain.User;
import com.yomic.drive.model.UserCreateModel;
import com.yomic.drive.repository.UserRepository;
import com.yomic.drive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return userRepository.findOne(Example.of(user)).orElse(null);
    }

    @Override
    public Long addUser(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = "{bcrypt}" + passwordEncoder.encode("123456");
        user.setPassword(encodedPassword);
        return userRepository.save(user).getId();
    }

    @Override
    public void deleteUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        List<User> deleteUserList = userRepository.findAll(Example.of(user));
        userRepository.deleteAll(deleteUserList);
    }
}
