package com.yomic.drive.web;

import com.yomic.drive.domain.Dept;
import com.yomic.drive.domain.User;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.helper.ValidationHelper;
import com.yomic.drive.model.DeptCreateModel;
import com.yomic.drive.service.DeptService;
import com.yomic.drive.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "部门相关接口")
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    @ApiOperation("获取部门列表")
    @GetMapping("/depts")
    public JsonResult<List<Dept>> getDepts(Long parentId) {
        return new JsonResult<>(deptService.getDeptList(parentId));
    }

    @ApiOperation("根据ID获取部门")
    @GetMapping("/depts/{id}")
    public JsonResult<Dept> getDept(@PathVariable Long id) {
        return new JsonResult<>(deptService.getDept(id));
    }

    @ApiOperation("添加部门")
    @PostMapping("/depts")
    public JsonResult<Dept> addDept (@Valid @RequestBody DeptCreateModel dept, BindingResult result) {
        ValidationHelper.validate(result);
        return new JsonResult<>(deptService.addDept(dept.toDomain()));
    }

}
