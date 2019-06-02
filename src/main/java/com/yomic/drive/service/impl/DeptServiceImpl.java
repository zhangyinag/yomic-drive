package com.yomic.drive.service.impl;

import com.yomic.drive.domain.Dept;
import com.yomic.drive.helper.AssertHelper;
import com.yomic.drive.helper.ExceptionHelper;
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
    public List<Dept> getDeptList(Long parentId) {
        Dept parent = null;
        if(parentId != null) {
            parent = deptRepository.findById(parentId)
                    .orElseThrow(ExceptionHelper.optionalThrow("未找到父级部门: " + parentId));
        }
        if (parent == null) return deptRepository.findDeptsByParentIsNull();
        return deptRepository.findDeptsByParent(parent);
    }

    @Override
    public Dept addDept(Dept dept) {
        return deptRepository.save(dept);
    }

    @Override
    public void deleteDeptById(Long id) {
        deptRepository.deleteById(id);
    }

    @Override
    public Dept getRootDept() {
        return deptRepository.findDeptsByParentIsNull().get(0);
    }

    @Override
    public Dept getDept(Long id) {
        AssertHelper.assertNotNull(id);
        return deptRepository.findById(id).orElse(null);
    }
}
