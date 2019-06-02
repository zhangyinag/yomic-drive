package com.yomic.drive.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yomic.drive.domain.File;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@MappedSuperclass
public class CascadeEntity<E extends CascadeEntity> extends BaseEntity{

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private E parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<E> children;

    public Long getParentId () {
        return parent != null ? parent.id : null;
    }

    public void setParentId (Long parentId) {
        E parent = null;
        try {
            if (parentId != null) {
                parent = (E)this.getClass().newInstance();
                parent.setId(parentId);
            }
            this.parent = parent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
