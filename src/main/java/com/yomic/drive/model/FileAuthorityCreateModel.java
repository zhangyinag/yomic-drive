package com.yomic.drive.model;

import com.yomic.drive.domain.FileAuthority;
import com.yomic.drive.domain.Role;
import com.yomic.drive.domain.User;
import com.yomic.drive.model.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel
@Data
public class FileAuthorityCreateModel extends BaseModel<FileAuthority> {

    @ApiModelProperty(required = true)
    @NotNull
    private Long sid;

    @ApiModelProperty(required = true)
    @NotNull
    private Long pid;

    @ApiModelProperty(required = true)
    @NotNull
    private Boolean principal;

    @ApiModelProperty()
    private Long authorities;

    @ApiModelProperty()
    private Long inherit;

}
