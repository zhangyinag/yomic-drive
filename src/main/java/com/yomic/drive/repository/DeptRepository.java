package com.yomic.drive.repository;

import com.yomic.drive.domain.Dept;
import com.yomic.drive.repository.common.BaseRepository;

import java.util.List;

public interface DeptRepository extends BaseRepository<Dept> {
    List<Dept> findDeptsByParent(Dept dept);

    List<Dept> findDeptsByParentIsNull();
}
