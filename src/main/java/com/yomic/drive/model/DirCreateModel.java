package com.yomic.drive.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class DirCreateModel {

    @NotNull
    @NotEmpty
    private String name;

    private Long parentId;

}
