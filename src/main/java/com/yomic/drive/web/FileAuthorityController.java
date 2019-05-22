package com.yomic.drive.web;

import com.yomic.drive.domain.File;
import com.yomic.drive.domain.FileAuthority;
import com.yomic.drive.helper.FileHelper;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.helper.ValidationHelper;
import com.yomic.drive.model.DirCreateModel;
import com.yomic.drive.model.FileAuthorityCreateModel;
import com.yomic.drive.repository.FileAuthorityRepository;
import com.yomic.drive.service.FileAuthorityService;
import com.yomic.drive.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Api(tags = "文件权限相关操作")
@RestController
public class FileAuthorityController {

    @Autowired
    private FileAuthorityService fileAuthorityService;

    @ApiOperation("添加文件权限")
    @PostMapping("/authorities")
    public JsonResult<Long> addAuthority (@Valid  FileAuthorityCreateModel model, BindingResult bindingResult) {
        ValidationHelper.validate(bindingResult);
        return JsonResult.success(fileAuthorityService.addFileAuthority(model.toDomain()));
    }

}
