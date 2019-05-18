package com.yomic.drive.web;

import com.yomic.drive.domain.File;
import com.yomic.drive.helper.FileHelper;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.service.FileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("上传文件")
    @PostMapping("/files")
    public JsonResult<Long> uploadFile (MultipartFile file) {
        Long id = fileService.saveFile(file, null, null);
        return new JsonResult<>(id);
    }

    @ApiOperation("下载文件")
    @PostMapping("/files/download/{id}")
    public ResponseEntity<FileSystemResource> uploadFile (@PathVariable Long id, HttpServletResponse response) {
        File file = fileService.downloadFile(id);
        return FileHelper.download(file, response);
    }

}
