package com.yomic.drive.service.impl;

import com.yomic.drive.domain.Dept;
import com.yomic.drive.domain.File;
import com.yomic.drive.domain.User;
import com.yomic.drive.helper.AssertHelper;
import com.yomic.drive.helper.ExceptionHelper;
import com.yomic.drive.repository.DeptRepository;
import com.yomic.drive.repository.FileRepository;
import com.yomic.drive.repository.UserRepository;
import com.yomic.drive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private FileRepository fileRepository;

    @Override
    public List<User> getUserList(Long deptId) {
        AssertHelper.assertNotNull(deptId);
        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(ExceptionHelper.optionalThrow("未找到用户归属部门：" + deptId));
        return userRepository.findUsersByDept(dept);
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
        List<File> fileList = user.getFileList();
        if (fileList != null) {
            List<File> ret = new ArrayList<>();
            for(File f : fileList) {
                File item = fileRepository.findById(f.getId()).orElse(null);
                if (item != null) ret.add(item);
            }
            user.setFileList(ret);
        }
        return userRepository.save(user).getId();
    }

    @Override
    public void deleteUser(Long id) {
        AssertHelper.assertNotNull(id);
        userRepository.deleteById(id);
    }
}
