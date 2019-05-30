package com.yomic.drive.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yomic.drive.domain.common.CascadeEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
@Entity
public class File extends CascadeEntity<File> {

    public static File forParent (Long id) {
        File file = new File();
        file.setId(id);
        return file;
    }

    private Boolean isDir;

    private String name;

    private String contentType;

    private Long size;

    private String uuid;

    private String uploadBy;

    private Date uploadDate;

    private String modifyBy;

    private Date modifyDate;

    private Boolean status;

    private Long limitSize;

    private String limitSuffix;

    private Boolean share;

    @Transient
    @JsonIgnore
    private java.io.File rawFile;

    @Transient
    private Long authorities;


    public boolean hasAuthorities(Long... bits) {
       return FileAuthority.hasAuthorities(this.authorities, bits);
    }

    @Transient
    public List<File> getParents() {
        List<File> parents = new ArrayList<>();
        File parent = this.getParent();
        while (parent != null) {
            File item = new File();
            item.setId(parent.getId());
            item.setName(parent.getName());
            parents.add(0, item);
            parent = parent.getParent();
        }
        return parents;
    }
}
