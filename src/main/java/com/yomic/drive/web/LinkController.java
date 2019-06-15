package com.yomic.drive.web;

import com.yomic.drive.domain.Link;
import com.yomic.drive.domain.User;
import com.yomic.drive.helper.JsonResult;
import com.yomic.drive.helper.ValidationHelper;
import com.yomic.drive.model.LinkCreateModel;
import com.yomic.drive.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "外链相关接口")
@RestController
public class LinkController {

    @Autowired
    private LinkService linkService;

    @ApiOperation("获取用户外链列表")
    @GetMapping("/links")
    public JsonResult<List<Link>> getLinks (Long deptId) {
        return new JsonResult<>(linkService.getLinks());
    }

    @ApiOperation("根据id获取外链")
    @GetMapping("/links/{id}")
    public JsonResult<Link> getLink (@PathVariable Long id) {
        return new JsonResult<>(linkService.getLink(id));
    }

    @ApiOperation("添加外链")
    @PostMapping("/links")
    public JsonResult<Long> addLink (@Valid @RequestBody LinkCreateModel link, BindingResult result) {
        ValidationHelper.validate(result);
        return new JsonResult<>(linkService.saveLink(link.toDomain()));
    }

    @ApiOperation("根据外链id删除外链")
    @DeleteMapping("/links/{id}")
    public JsonResult deleteLink (@PathVariable Long id) {
        linkService.deleteLink(id);
        return new JsonResult();
    }
}
