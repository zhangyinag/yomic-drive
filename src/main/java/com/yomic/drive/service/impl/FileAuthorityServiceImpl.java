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
        return getAuthorityFromDept(sid, pid, null, null);
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
        FileAuthority authority = getAuthorityFromFile(sid, pid, true, null, null);
        if(authority.hasInheritAll()) return authority;
        if(user.getDeptId() != null) {
            FileAuthority temp = getAuthorityFromDept(user.getDeptId(), pid, authority.getImplicitAuthorities(), authority.getImplicitInherit());
            authority.setImplicitInherit(temp.getImplicitInherit());
            authority.setImplicitAuthorities(temp.getImplicitAuthorities());
        }
        return authority;
    }

    private FileAuthority getAuthorityFromDept(Long sid, Long pid, Long baseAuthorities, Long baseInherit) {
        Dept current = deptRepository.findById(sid).orElseThrow(() -> new RuntimeException("not found dept: " + sid));
        List<Dept> ancestors = new ArrayList<>();
        while(current != null) {
            ancestors.add(current);
            current = current.getParent();
        }

        FileAuthority authority = null;
        for (int i = 0; i < ancestors.size(); i++) {
            Dept s = ancestors.get(i);
            Long authorities = authority == null ? baseAuthorities : authority.getImplicitAuthorities();
            Long inherit = authority == null ? baseInherit : authority.getImplicitInherit();
            FileAuthority temp = getAuthorityFromFile(s.getId(), pid, false, authorities, inherit);
            if(authority == null) authority = temp;
            else{
                authority.setImplicitInherit(temp.getImplicitInherit());
                authority.setAuthorities(temp.getImplicitAuthorities());
            }
            if(authority.hasInheritAll()) break;
        }
        return authority;
    }

    /**
     * 获取指定 用户（组织）对 指定的文件的权限
     * @param sid
     * @param pid
     * @param principal
     * @return
     */
    private FileAuthority getAuthorityFromFile(Long sid, Long pid, Boolean principal, Long baseAuthorities, Long baseInherit) {
        File current = fileRepository.findById(pid).orElseThrow(() -> new RuntimeException("not found file: " + pid));
        List<File> ancestors = new ArrayList<>();
        while(current != null && current.getStatus()) {
            ancestors.add(current);
            current = current.getParent();
        }

        Long authorities = baseAuthorities == null ? 0L : baseAuthorities;
        Long inherit = baseInherit == null ? -1L : baseInherit;
        Map<Long, Long> inheritMap = new HashMap<>();
        FileAuthority ret = new FileAuthority();
        for (int i = 0; i < ancestors.size(); i++) {
            File s = ancestors.get(i);
            FileAuthority fileAuthority = find(sid, s.getId(), principal).orElse(null);
            if(fileAuthority != null){
                if(fileAuthority.getSid() == sid && fileAuthority.getPid() == pid){
                    ret.setAuthorities(fileAuthority.getAuthorities());
                    ret.setInherit(fileAuthority.getInherit());
                }
                Long temp = authorities;
                authorities = fileAuthority.getAuthorities() | authorities & inherit;
                inherit = fileAuthority.getInherit() & inherit;
                inheritMap.put(temp ^ authorities, fileAuthority.getId());
                // 已完成继承 无需向上查找
                if(FileAuthority.hasInheritAll(authorities, inherit)) break;
            }
        }
        ret.setImplicitAuthorities(authorities);
        ret.setImplicitInherit(inherit);
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
