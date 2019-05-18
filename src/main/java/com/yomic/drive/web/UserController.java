package com.yomic.drive.web;

import com.yomic.drive.domain.User;
import com.yomic.drive.exception.ValidationException;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.model.UserModel;
import com.yomic.drive.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public JsonResult<String> addUser (@Valid UserModel user,
                                     BindingResult result) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors());
        }
        userService.addUser(user);
        return new JsonResult<>(user.getUsername());
    }

    @ApiOperation("根据用户名删除用户")
    @DeleteMapping("/users/{username}")
    public JsonResult deleteUserByUsername (@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return new JsonResult();
    }
}
