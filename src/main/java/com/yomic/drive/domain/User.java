package com.yomic.drive.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yomic.drive.domain.common.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class User extends BaseEntity {

    @NotNull
    @Column(nullable = false, unique = true, length = 128)
    private String username;

    private String cname;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String password;

    private String ip;

    private Boolean status;

    /**
     * 联合主键在MySQL中有长度限制，所以对两个字段的长度做了限制
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "roleCode", referencedColumnName = "roleCode", nullable = false),
            uniqueConstraints = {@UniqueConstraint(columnNames={"username", "roleCode"})})
    private List<Role> roleList;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Dept dept;

    public Long getDeptId () {
        return dept != null ? dept.getId() : null;
    }

    public void setDeptId (Long deptId) {
        Dept dept = null;
        if (deptId != null) {
            dept = new Dept();
            dept.setId(deptId);
        }
        this.dept = dept;
    }
}
