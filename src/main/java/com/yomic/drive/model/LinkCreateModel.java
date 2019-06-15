package com.yomic.drive.model;

import com.yomic.drive.domain.File;
import com.yomic.drive.domain.Link;
import com.yomic.drive.domain.Role;
import com.yomic.drive.domain.User;
import com.yomic.drive.helper.ContextHelper;
import com.yomic.drive.model.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel
@Data
public class LinkCreateModel extends BaseModel<Link> {

    @ApiModelProperty(value = "文件列表", example = "user", required = true)
    @NotNull
    @NotEmpty
    private Long[] files;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "有效天数")
    private Integer effectiveDays;


    @Override
    public Link toDomain() {
       Link domain = super.toDomain();
        if (files != null) {
            List<File> fileList = new ArrayList<>();
            for(Long id : files) {
                if (id != null) {
                    File item = new File();
                    item.setId(id);
                    fileList.add(item);
                }
            }
            domain.setFileList(fileList);
        }
        domain.setGenerateDate(new Date());
        domain.setGenerateBy(ContextHelper.getCurrentUsername());
        return domain;
    }
}
