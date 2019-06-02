package com.yomic.drive.service;

import com.yomic.drive.domain.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> getDeptList (Long parentId);

    Dept addDept (Dept dept);

    void deleteDeptById (Long id);

    Dept getRootDept ();

    Dept getDept (Long id);
}
