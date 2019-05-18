package com.yomic.drive.domain;

import com.yomic.drive.domain.common.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
public class Role extends BaseEntity {

    public Role() {
    }

    public Role(String roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    @Column(nullable = false, unique = true, length = 64)
    private String roleCode;

    @Column(nullable = false)
    private String roleName;

}
