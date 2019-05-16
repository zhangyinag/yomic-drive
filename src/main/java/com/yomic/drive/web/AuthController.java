package com.yomic.drive.web;

import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.constant.HttpJsonStatus;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    /**
     * 这是一个形式接口，真正处理逻辑由 Spring Security 管理
     * 请参见 {@link com.yomic.drive.config.WebSecurityConfig}
     * @return
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public JsonResult login (String username, String password) {
        return new JsonResult(HttpJsonStatus.SUCCESS);
    }

    /**
     * 这是一个形式接口，真正处理逻辑由 Spring Security 管理
     * 请参见 {@link com.yomic.drive.config.WebSecurityConfig}
     * @return
     */
    @ApiOperation("登出")
    @PostMapping("/logout")
    public JsonResult logout () {
        return new JsonResult(HttpJsonStatus.SUCCESS);
    }
}
