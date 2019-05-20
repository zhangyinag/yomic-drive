package com.yomic.drive.domain;

import com.yomic.drive.domain.common.BaseEntity;
import com.yomic.drive.domain.common.CascadeEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Transient;


@Data
@Entity
public class FileAuthority extends BaseEntity {

    private Long sid;

    private Long pid;

    private Long authorities;

    private Long pit;

    private Long sit;

    private Boolean principal;


    public Long getAuthorities(){
        return authorities == null ? 0l : authorities;
    }

    public Long getPit() {
        return pit == null ? -1l : pit;
    }

    public Long getSit() {
        return sit == null ? -1l : sit;
    }
}

