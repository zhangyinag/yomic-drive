package com.yomic.drive.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yomic.drive.domain.common.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@Entity
public class Link extends BaseEntity {

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "link_file",
            joinColumns = @JoinColumn(name = "linkId", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "fileId", referencedColumnName = "id", nullable = false),
            uniqueConstraints = {@UniqueConstraint(columnNames={"linkId", "fileId"})})
    private List<File> fileList;

    private String password;

    private Date generateDate;

    private String generateBy;

    private Integer effectiveDays;


    @Transient
    public List<File> getFiles () {
        List<File> ret = new ArrayList<>();
        if (fileList != null) {
            for(File f : fileList) {
                f.setParent(null);
                f.setChildren(null);
                ret.add(f);
            }
        }
        return ret;
    }
}

