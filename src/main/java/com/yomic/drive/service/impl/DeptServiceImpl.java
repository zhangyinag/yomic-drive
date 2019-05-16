package com.yomic.drive.service.impl;

import com.yomic.drive.domain.Dept;
import com.yomic.drive.repository.DeptRepository;
import com.yomic.drive.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptRepository deptRepository;

    @Override
    public List<Dept> getDeptList() {
        return deptRepository.findAll();
    }

    @Override
    public Dept addDept(Dept dept) {
        return deptRepository.save(dept);
    }

    @Override
    public void deleteDeptById(Long id) {
        deptRepository.deleteById(id);
    }
}
