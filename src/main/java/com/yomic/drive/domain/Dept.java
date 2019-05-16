package com.yomic.drive.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Dept {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToOne()
    private Dept parent;
}
