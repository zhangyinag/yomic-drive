package com.yomic.drive.domain;

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

    public static boolean hasAuthorities(Long authorities, Long... bits) {
        Long sum = 0L;
        for(int i = 0; i < bits.length; i++) {
            sum = sum | bits[i];
        }
        return (sum & authorities) == sum;
    }

    public static boolean hasFullAuthorities(Long authorities) {
        return authorities == -1L;
    }

    public static boolean hasFullBreak(Long inherit) {
        return inherit == 0L;
    }

    private Long sid;

    private Long pid;

    private Long authorities;

    private Long inherit;

    private Boolean principal;

    @Transient
    private Boolean implicit;

    @Transient
    private Map<Long, Long> inheritMap = new HashMap<>();


    public Long getAuthorities(){
        return authorities == null ? 0L : authorities;
    }

    public Long getInherit() {
        return inherit == null ? -1L : inherit;
    }


}

