package com.yomic.drive.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yomic.drive.domain.common.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;


@Data
@Entity
public class FileAuthority extends BaseEntity {

    public static final Long VISIBLE            = 0b1L;     // 可见
    public static final Long NEW                = 0b10L;     // 新建
    public static final Long UPDATE             = 0b100L;    // 修改
    public static final Long RENAME             = 0b1000L;    //重命名
    public static final Long DELETE             = 0b10000L;    // 删除
    public static final Long PREVIEW            = 0b100000L;    // 预览
    public static final Long DOWNLOAD           = 0b1000000L;    // 下载
    public static final Long LINK               = 0b10000000L;    // 外链

    private static final Long BITS = 0b1111_1111L;

    public static boolean hasAuthorities(Long authorities, Long... bits) {
        if (authorities == null) authorities = 0L;
        Long sum = 0L;
        for(int i = 0; i < bits.length; i++) {
            sum = sum | bits[i];
        }
        return (sum & authorities) == sum;
    }

    public static boolean hasFullAuthorities(Long authorities) {
        return (authorities & FileAuthority.BITS) == FileAuthority.BITS;
    }


    private Long sid;

    private Long pid;

    private Long authorities;

    private Boolean principal;

    @Transient
    @JsonIgnore
    private File file;

    @Transient
    @JsonIgnore
    private User user;

    @Transient
    @JsonIgnore
    private Dept dept;

    public Long getAuthorities(){
        return authorities == null ? 0L : authorities;
    }


    public boolean hasFullAuthorities() {
        return FileAuthority.hasFullAuthorities(this.getAuthorities());
    }

    public String getUsername () {
        return this.user == null ? null : this.user.getUsername();
    }

    public String getDeptName () {
        return this.dept == null ? null : this.dept.getName();
    }

    public String getFileName () {
        return this.file == null ? null : this.file.getName();
    }

    public Boolean getDir () {
        return this.file == null ? null : this.file.getIsDir();
    }

    public String getContentType () {
        return this.file == null ? null : this.file.getContentType();
    }
}

