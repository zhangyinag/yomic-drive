package com.yomic.drive.domain;

import com.yomic.drive.domain.common.CascadeEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;

@ApiModel
@Data
@Entity
public class Dept extends CascadeEntity<Dept> {

    private String name;
}
