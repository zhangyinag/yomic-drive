package com.yomic.drive.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Example {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Example() {
    }

    public Example(String name) {
        this.name = name;
    }
}
