package com.yomic.drive.web;

import com.yomic.drive.domain.User;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.helper.ValidationHelper;
import com.yomic.drive.model.UserCreateModel;
import com.yomic.drive.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "用户相关接口")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("获取用户列表")
    @GetMapping("/users")
    public JsonResult<List<User>> getUsers (Long deptId) {
        return new JsonResult<>(userService.getUserList(deptId));
    }

    @ApiOperation("添加用户")
    @PostMapping("/users")
    public JsonResult<Long> addUser (@Valid @RequestBody UserCreateModel user, BindingResult result) {
        ValidationHelper.validate(result);
        return new JsonResult<>(userService.addUser(user.toDomain()));
    }

    @ApiOperation("根据用户名删除用户")
    @DeleteMapping("/users/{username}")
    public JsonResult deleteUserByUsername (@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return new JsonResult();
    }
}
