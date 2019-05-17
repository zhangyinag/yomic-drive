package com.yomic.drive.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yomic.drive.domain.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User extends BaseEntity {

    @NotNull
    private String username;

    private String cname;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String password;

    private String ip;

    @ApiModelProperty(hidden = true)
    private boolean status;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "username"), inverseJoinColumns = @JoinColumn(name = "role_code"))
    private List<Role> roleList;

    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Dept dept;
}
