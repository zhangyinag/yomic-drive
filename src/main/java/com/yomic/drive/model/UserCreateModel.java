package com.yomic.drive.model;

import com.yomic.drive.domain.File;
import com.yomic.drive.domain.Role;
import com.yomic.drive.domain.User;
import com.yomic.drive.model.common.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel
@Data
public class UserCreateModel extends BaseModel<User> {

    @ApiModelProperty(example = "user", required = true)
    @NotNull
    @NotEmpty
    private String username;

    private String ip;

    @ApiModelProperty(example = "普通用户", required = true)
    @NotNull
    @NotEmpty
    private String cname;

    @ApiModelProperty(value = "所属机构")
    @NotNull
    private Long deptId;

    @ApiModelProperty(value = "拥有角色", example = "USER")
    @NotEmpty
    @NotNull
    private String[] roles;

    @ApiModelProperty(value = "管理文件列表", example = "")
    private Long[] files;


    @Override
    public User toDomain() {
       User domain = super.toDomain();
        if (roles != null) {
            List<Role> roleList = Arrays.stream(roles).map(s -> new Role(s, "")).collect(Collectors.toList());
            domain.setRoleList(roleList);
        }
        if (files != null) {
            List<File> fileList = new ArrayList<>();
            for(Long id : files) {
                if(id != null) {
                    File item = new File();
                    item.setId(id);
                    fileList.add(item);
                }
            }
            domain.setFileList(fileList);
        }
        domain.setStatus(true);
        return domain;
    }
}
