package com.yomic.drive.web;

import com.yomic.drive.domain.File;
import com.yomic.drive.domain.RecycleFile;
import com.yomic.drive.helper.FileHelper;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.helper.ValidationHelper;
import com.yomic.drive.model.DirCreateModel;
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

@Api(tags = "文件相关操作")
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("获取文件列表")
    @GetMapping("/files")
    public JsonResult<List<File>> getFiles (Long parentId, Boolean isDir) {
        return JsonResult.success(fileService.getFiles(parentId, isDir));
    }

    @ApiOperation("获取管理文件列表")
    @GetMapping("/files/admin")
    public JsonResult<List<File>> getAdminFiles (Long parentId, Boolean isDir) {
        return JsonResult.success(fileService.getFilesForAuthority(parentId, isDir));
    }

    @ApiOperation("根据ID获取文件")
    @GetMapping("/files/{id}")
    public JsonResult<File> getFile (@PathVariable Long id) {
        return JsonResult.success(fileService.getFile(id));
    }

    @ApiOperation("新建文件夹")
    @PostMapping("/files/dir")
    public JsonResult<Long> addDir (@Valid @RequestBody DirCreateModel model, BindingResult result) {
        ValidationHelper.validate(result);
        return JsonResult.success(fileService.saveDir(model.getName(), model.getParentId()));
    }

    @ApiOperation("上传文件")
    @PostMapping("/files")
    public JsonResult<Long> uploadFile (MultipartFile file, Long parentId) {
        Long id = fileService.saveFile(file, parentId);
        return new JsonResult<>(id);
    }

    @ApiOperation("更新文件")
    @PutMapping("/files/{id}")
    public JsonResult<Long> updateFile (MultipartFile file, @PathVariable Long id, String desc) {
        return new JsonResult<>(fileService.updateFile(file, id, desc));
    }

    @ApiOperation("下载文件")
    @GetMapping("/files/download/{id}")
    public ResponseEntity<FileSystemResource> uploadFile (@PathVariable Long id, HttpServletResponse response) {
        File file = fileService.downloadFile(id);
        return FileHelper.download(file, response);
    }

    @ApiOperation("预览文件")
    @GetMapping("/files/preview/{id}")
    public ResponseEntity<FileSystemResource> previewFile (@PathVariable Long id, HttpServletResponse response) {
        File file = fileService.downloadFile(id);
        return FileHelper.preview(file, response);
    }

    @ApiOperation("重命名文件")
    @PostMapping("/files/rename/{id}")
    public JsonResult<Long> renameFile (@PathVariable Long id, @NotEmpty @RequestParam String name) {
        return JsonResult.success(fileService.rename(id, name));
    }

    @ApiOperation("获取回收文件列表")
    @GetMapping("/files/recycle")
    public JsonResult<List<RecycleFile>> getRecycleFiles () {
        return JsonResult.success(fileService.getRecycleFiles());
    }

    @ApiOperation("回收文件")
    @PutMapping("/files/recycle/{id}")
    public JsonResult recycleFile (@PathVariable Long id) {
        fileService.recycleFile(id);
        return JsonResult.success(null);
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/files/{id}")
    public JsonResult deleteFile (@PathVariable Long id) {
        fileService.deleteFile(id);
        return JsonResult.success(null);
    }

    @ApiOperation("批量回收文件")
    @PutMapping("/files/batch/recycle")
    public JsonResult batchRecycleFiles (@RequestBody Long ids) {
        return JsonResult.success(null);
    }

    @ApiOperation("批量删除文件")
    @DeleteMapping("/files/batch")
    public JsonResult batchDeleteFiles (@RequestBody Long ids) {
        return JsonResult.success(null);
    }

}
