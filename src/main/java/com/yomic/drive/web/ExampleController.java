package com.yomic.drive.web;

import com.yomic.drive.domain.Example;
import com.yomic.drive.service.ExampleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExampleController {

    @Autowired
    ExampleService exampleService;

    @ApiOperation("获取example列表")
    @GetMapping("/examples")
    public List<Example> getExamples() {
        return exampleService.getExampleList();
    }

    @ApiOperation("添加一个example对象")
    @PostMapping("/examples/")
    public Example addExample(@RequestBody Example example) {
        return exampleService.addExample(example);
    }
}
