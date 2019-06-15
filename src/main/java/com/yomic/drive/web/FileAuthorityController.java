package com.yomic.drive.web;

import com.yomic.drive.domain.FileAuthority;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.helper.ValidationHelper;
import com.yomic.drive.model.FileAuthorityCreateModel;
import com.yomic.drive.service.FileAuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "文件权限相关操作")
@RestController
public class FileAuthorityController {

    @Autowired
    private FileAuthorityService fileAuthorityService;

    @ApiOperation("添加文件权限")
    @PostMapping("/authorities")
    public JsonResult<Long> addAuthority (@Valid  @RequestBody FileAuthorityCreateModel model, BindingResult bindingResult) {
        ValidationHelper.validate(bindingResult);
        return JsonResult.success(fileAuthorityService.addFileAuthority(model.toDomain()));
    }

    @ApiOperation("查看文件权限列表")
    @GetMapping("/authorities")
    public JsonResult<List<FileAuthority>> getAuthorities (Long sid, Long pid, Boolean principal) {
        if (sid != null && principal != null && pid != null) {
            FileAuthority fa = fileAuthorityService.getFileAuthority(sid, pid, principal);
            List<FileAuthority> ret = new ArrayList<>();
            if (fa != null) ret.add(fa);
            return JsonResult.success(ret);
        }
        if (sid != null && principal != null)
            return JsonResult.success(fileAuthorityService.getFileAuthoritiesBySid(sid, principal));
        if (pid != null) return JsonResult.success(fileAuthorityService.getFileAuthoritiesByPid(pid));
        throw new RuntimeException("参数不完整");
    }

    @ApiOperation("删除文件权限")
    @DeleteMapping("/authorities/{id}")
    public JsonResult deleteAuthority (@PathVariable Long id) {
        fileAuthorityService.deleteFileAuthority(id);
        return JsonResult.success(null);
    }

    @ApiOperation("更新文件权限")
    @PutMapping("/authorities/{id}")
    public JsonResult deleteAuthority (@PathVariable Long id, Long authorities) {
        return JsonResult.success(fileAuthorityService.updateFileAuthority(id, authorities));
    }

}
