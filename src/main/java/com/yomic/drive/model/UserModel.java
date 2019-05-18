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
public class UserModel extends BaseModel<User> {

    @ApiModelProperty(example = "user")
    @NotNull
    private String username;

    private String ip;

    @ApiModelProperty(example = "普通用户")
    @NotNull
    private String cname;

    @ApiModelProperty(value = "所属机构")
    private Long deptId;

    @ApiModelProperty(value = "拥有角色", example = "USER")
    private String[] roles;


    @Override
    public User toDomain() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        if (roles != null) {
            List<Role> roleList = Arrays.stream(roles).map(s -> new Role(s, "")).collect(Collectors.toList());
            user.setRoleList(roleList);
        }
        return user;
    }
}
