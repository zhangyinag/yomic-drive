package com.yomic.drive.model.common;

import com.yomic.drive.domain.Role;
import com.yomic.drive.domain.User;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseModel<E> {

    public E toDomain () {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class clz = (Class<E>) type.getActualTypeArguments()[0];
        try {
            E domain = (E)clz.newInstance();
            BeanUtils.copyProperties(this, domain);
            return domain;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
