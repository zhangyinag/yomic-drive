package com.yomic.drive.service.impl;

import com.yomic.drive.domain.Dept;
import com.yomic.drive.domain.File;
import com.yomic.drive.domain.FileAuthority;
import com.yomic.drive.domain.User;
import com.yomic.drive.repository.DeptRepository;
import com.yomic.drive.repository.FileAuthorityRepository;
import com.yomic.drive.repository.FileRepository;
import com.yomic.drive.repository.UserRepository;
import com.yomic.drive.service.FileAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class FileAuthorityServiceImpl implements FileAuthorityService {

    @Autowired
    private FileAuthorityRepository fileAuthorityRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeptRepository deptRepository;


    @Override
    public FileAuthority getFileAuthority(Long sid, Long pid, Boolean principal) {
        if(principal) return getAuthorityFromUser(sid, pid);
        return getAuthorityFromDept(sid, pid);
    }

    @Override
    public Long addFileAuthority(FileAuthority authority) {
        FileAuthority exist = find(authority.getSid(), authority.getPid(), authority.getPrincipal()).orElse(null);
        if(exist != null) fileAuthorityRepository.deleteById(authority.getId());
        authority = fileAuthorityRepository.saveAndFlush(authority);
        return authority.getId();
    }

    @Override
    public Long updateFileAuthority(Long id, Long authorities, Long inherit) {
        FileAuthority authority = fileAuthorityRepository.findById(id).orElseThrow(() -> new RuntimeException("not found authority: " + id));
        authority.setAuthorities(authorities);
        authority.setInherit(inherit);
        fileAuthorityRepository.save(authority);
        return id;
    }

    @Override
    public void deleteFileAuthority(Long id) {
        fileAuthorityRepository.deleteById(id);
    }

    private FileAuthority getAuthorityFromUser(Long sid, Long pid) {
        User user = userRepository.findById(sid).orElseThrow(() -> new RuntimeException("not found user: " + sid));
        FileAuthority baseAuthority = getAuthorityFromFile(sid, pid, true);
        if(baseAuthority.getImplicit()) return baseAuthority;
        if(user.getDeptId() != null) {
            baseAuthority = getAuthorityFromDept(user.getDeptId(), pid);
        }
        return baseAuthority;
    }

    private FileAuthority getAuthorityFromDept(Long sid, Long pid) {
        Dept current = deptRepository.findById(sid).orElseThrow(() -> new RuntimeException("not found dept: " + sid));
        List<Dept> ancestors = new ArrayList<>();
        while(current != null) {
            ancestors.add(current);
            current = current.getParent();
        }

        FileAuthority baseAuthority = null;
        for (int i = 0; i < ancestors.size(); i++) {
            Dept s = ancestors.get(i);
            baseAuthority = getAuthorityFromFile(s.getId(), pid, false);
            if(baseAuthority.getImplicit()) break;
        }
        return baseAuthority;
    }

    /**
     * 获取指定 用户（组织）对 指定的文件的权限
     * @param sid
     * @param pid
     * @param principal
     * @return
     */
    private FileAuthority getAuthorityFromFile(Long sid, Long pid, Boolean principal) {
        File current = fileRepository.findById(pid).orElseThrow(() -> new RuntimeException("not found file: " + pid));
        List<File> ancestors = new ArrayList<>();
        while(current != null) {
            ancestors.add(current);
            current = current.getParent();
        }

        long baseInherit = -1L;
        Long authorities = 0L;
        Long inherit = -1L;
        Map<Long, Long> inheritMap = new HashMap<>();
        boolean implicit = false;
        for (int i = 0; i < ancestors.size(); i++) {
            File s = ancestors.get(i);
            FileAuthority fileAuthority = find(sid, s.getId(), principal).orElse(null);
            if(fileAuthority != null){
                implicit = true;
                if(i == 0){
                    baseInherit = fileAuthority.getInherit();
                }
                Long temp = authorities;
                authorities = fileAuthority.getAuthorities() | authorities & inherit;
                inherit = fileAuthority.getInherit() & inherit;
                inheritMap.put(temp ^ authorities, fileAuthority.getId());
                // 权限已为全部， 不再向上查询
                if(FileAuthority.hasFullAuthorities(authorities)) break;
                // 继承关系已全部中断，不再向上查询
                if(FileAuthority.hasFullBreak(inherit)) break;
            }
        }
        FileAuthority ret = new FileAuthority();
        ret.setAuthorities(authorities); // 层级查找获取的权限
        ret.setInherit(baseInherit); // 继承关系
        ret.setImplicit(implicit); // 该用户是否有直接设置该文件及其父级的权限
        ret.setInheritMap(inheritMap); // 记录权限的继承位置
        return ret;
    }

    private Optional<FileAuthority> find(Long sid, Long pid, Boolean principal){
        FileAuthority prod = new FileAuthority();
        prod.setSid(sid);
        prod.setPid(pid);
        prod.setPrincipal(principal);
        return fileAuthorityRepository.findOne(Example.of(prod));
    }
}
