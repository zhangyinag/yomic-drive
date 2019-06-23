package com.yomic.drive.domain;

import com.yomic.drive.domain.common.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;


@Data
@Entity
public class FileTrack extends BaseEntity {

    private Long fileId;

    private Date modifiedDate;

    private String modifiedBy;

    private Long fileSize;

    private String desc;
}
