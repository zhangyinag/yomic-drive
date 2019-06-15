package com.yomic.drive.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yomic.drive.domain.common.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
public class User extends BaseEntity implements UserDetails {

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
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "roleCode", referencedColumnName = "roleCode", nullable = false),
            uniqueConstraints = {@UniqueConstraint(columnNames={"username", "roleCode"})})
    private List<Role> roleList;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Dept dept;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_file",
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "fileId", referencedColumnName = "id", nullable = false),
            uniqueConstraints = {@UniqueConstraint(columnNames={"userId", "fileId"})})
    private List<File> fileList;

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

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> ret = new ArrayList<>();
        for(int i=0; i<roleList.size(); i++) {
            final Role role = roleList.get(i);
            GrantedAuthority s = () -> role.getRoleCode();
            ret.add(s);
        }
        return ret;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isEnabled() {
        return this.status;
    }


    public boolean isSuper(){
        return getAuthorities().stream().anyMatch(s -> ((GrantedAuthority) s).getAuthority().equals(Role.ROLE_SUPER));
    }

    public boolean isAdmin(){
        return getAuthorities().stream().anyMatch(s -> ((GrantedAuthority) s).getAuthority().equals(Role.ROLE_ADMIN));
    }

    @Transient
    public List<File> getFiles () {
        List<File> ret = new ArrayList<>();
        if (getFileList() == null) return ret;
        for (File f: getFileList()){
            File item = new File();
            item.setName(f.getName());
            item.setId(f.getId());
            ret.add(item);
        }
        return ret;
    }
}
