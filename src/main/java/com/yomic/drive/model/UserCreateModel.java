package com.yomic.drive.model;

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
public class UserCreateModel extends BaseModel<User> {

    @ApiModelProperty(example = "user", required = true)
    @NotNull
    private String username;

    private String ip;

    @ApiModelProperty(example = "普通用户", required = true)
    @NotNull
    private String cname;

    @ApiModelProperty(value = "所属机构")
    @NotNull
    private Long deptId;

    @ApiModelProperty(value = "拥有角色", example = "USER")
    private String[] roles;


    @Override
    public User toDomain() {
       User domain = super.toDomain();
        if (roles != null) {
            List<Role> roleList = Arrays.stream(roles).map(s -> new Role(s, "")).collect(Collectors.toList());
            domain.setRoleList(roleList);
        }
        domain.setStatus(true);
        return domain;
    }
}
