package com.yomic.drive.web;

import com.yomic.drive.constant.AuthoritiesConstant;
import com.yomic.drive.domain.Example;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.service.ExampleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExampleController {

    @Autowired
    ExampleService exampleService;

    @ApiOperation("获取example列表")
    @GetMapping("/examples")
    public JsonResult<List<Example>> getExamples() {
        return new JsonResult<>(exampleService.getExampleList());
    }

//    @Secured({AuthoritiesConstant.ROLE_ADMIN})
    @ApiOperation("添加一个example对象")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/examples/")
    public JsonResult<Example> addExample(@RequestBody Example example) {
        return new JsonResult<>(exampleService.addExample(example));
    }


    @ApiOperation("模拟一个异常")
    @PostMapping("/examples/exception")
    public JsonResult addExample() {
        throw new RuntimeException("test exception");
    }
}
