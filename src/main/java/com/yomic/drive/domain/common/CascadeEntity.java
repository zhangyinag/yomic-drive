package com.yomic.drive.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@ApiModel()
@Data
@MappedSuperclass
public class CascadeEntity<E extends CascadeEntity> extends BaseEntity{

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private E parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<E> children;

    public Long getParentId () {
        return parent != null ? parent.id : null;
    }

    public void setParentId (Long parentId) {
        try {
            E parent = (E)this.getClass().newInstance();
            parent.setId(parentId);
            this.parent = parent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
