package com.yomic.drive.web;

import com.yomic.drive.domain.Dept;
import com.yomic.drive.domain.User;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.service.DeptService;
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
public class EmpController {

    @Autowired
    private DeptService deptService;

    @ApiOperation("获取部门列表")
    @GetMapping("/depts")
    public JsonResult<List<Dept>> getUsers () {
        return new JsonResult<>(deptService.getDeptList());
    }

    @ApiOperation("添加部门")
    @PostMapping("/depts")
    public JsonResult<Dept> addDept (@NotNull Dept dept) {
        return new JsonResult<>(deptService.addDept(dept));
    }

}
