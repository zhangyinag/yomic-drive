package com.yomic.drive.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yomic.drive.domain.common.CascadeEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Date;


@Data
@Entity
public class File extends CascadeEntity<File> {

    private Boolean isDir;

    private String name;

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
}
