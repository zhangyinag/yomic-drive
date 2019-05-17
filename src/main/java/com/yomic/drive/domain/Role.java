package com.yomic.drive.domain;

import com.yomic.drive.domain.common.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "roleCode"))
public class Role extends BaseEntity {

    private String roleCode;

    private String roleName;

}
