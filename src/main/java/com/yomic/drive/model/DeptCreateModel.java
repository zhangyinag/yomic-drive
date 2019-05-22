package com.yomic.drive.model;

import com.yomic.drive.domain.Dept;
import com.yomic.drive.model.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class DeptCreateModel extends BaseModel<Dept> {

    @ApiModelProperty(required = true)
    @NotNull
    @NotEmpty
    private String name;

    @ApiModelProperty(required = true)
    @NotNull
    private Long parentId;
}
