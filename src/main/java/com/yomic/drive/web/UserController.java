package com.yomic.drive.web;

import com.yomic.drive.domain.User;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("获取用户列表")
    @GetMapping("/users")
    public JsonResult<List<User>> getUsers () {
        return new JsonResult<>(userService.getUserList());
    }

    @ApiOperation("添加用户")
    @PostMapping("/users")
    public JsonResult<User> addUser (@NotNull User user) {
        return new JsonResult<>(userService.addUser(user));
    }

    @ApiOperation("根据用户名删除用户")
    @PostMapping("/users/{username}")
    public JsonResult deleteUserByUsername (@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return new JsonResult();
    }
}
