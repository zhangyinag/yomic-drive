package com.yomic.drive.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yomic.drive.domain.common.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
public class RecycleFile extends BaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private File file;

    private Date recycleDate;

    private String recycleBy;

    @Transient
    public Long getFileId () {
        return file != null ? file.getId() : null;
    }

    @Transient
    public String getFileName () {
        return file != null ? file.getName() : null;
    }

    @Transient
    public String getFileContentType () {
        return file != null ? file.getContentType() : null;
    }

    @Transient
    public Boolean getFileIsDir () {
        return file != null ? file.getIsDir() : null;
    }

    @Transient
    public String getFilePath () {
        if(file == null || file.getParents() == null) return null;
        String path = file.getName();
        for (File f : file.getParents()) {
            path = f.getName() + '/' + path;
        }
        return path;
    }

}

