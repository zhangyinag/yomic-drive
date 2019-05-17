package com.yomic.drive.domain;

import com.yomic.drive.domain.common.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Example extends BaseEntity {

    private String name;

    public Example() {
    }

    public Example(String name) {
        this.name = name;
    }
}
