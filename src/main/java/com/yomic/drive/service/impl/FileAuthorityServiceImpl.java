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
    public Long getAuthorities(Long sid, Long pid, Boolean principal) {
        if(principal) return getAuthoritiesFromUser(sid, pid);
        return getAuthoritiesFromDept(sid, pid);
    }

    @Override
    public FileAuthority getFileAuthority(Long sid, Long pid, Boolean principal) {
        return fileAuthorityRepository.findFileAuthorityBySidAndPidAndPrincipal(sid, pid, principal);
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
        fileAuthorityRepository.save(authority);
        return id;
    }

    @Override
    public void deleteFileAuthority(Long id) {
        fileAuthorityRepository.deleteById(id);
    }

    private Long getAuthoritiesFromUser(Long sid, Long pid) {
        User user = userRepository.findById(sid).orElseThrow(() -> new RuntimeException("not found user: " + sid));
        Long authorities = getAuthoritiesFromFile(sid, pid, true);
        if(authorities != null) return authorities;
        if(user.getDeptId() != null) {
            authorities = getAuthoritiesFromDept(user.getDeptId(), pid);
            if(authorities != null) return authorities;
        }
        return authorities;
    }

    private Long getAuthoritiesFromDept(Long sid, Long pid) {
        Dept current = deptRepository.findById(sid).orElseThrow(() -> new RuntimeException("not found dept: " + sid));
        List<Dept> ancestors = new ArrayList<>();
        while(current != null) {
            ancestors.add(current);
            current = current.getParent();
        }

        Long authorities = null;
        for (int i = 0; i < ancestors.size(); i++) {
            Dept s = ancestors.get(i);
            authorities = getAuthoritiesFromFile(s.getId(), pid, false);
            if(authorities != null) break;
        }
        return authorities;
    }

    /**
     * 获取指定 用户（组织）对 指定的文件的权限
     * @param sid
     * @param pid
     * @param principal
     * @return
     */
    private Long getAuthoritiesFromFile(Long sid, Long pid, Boolean principal) {
        File current = fileRepository.findById(pid).orElseThrow(() -> new RuntimeException("not found file: " + pid));
        List<File> ancestors = new ArrayList<>();
        while(current != null && current.getStatus()) {
            ancestors.add(current);
            current = current.getParent();
        }
        Long authorities = null;
        for (int i = 0; i < ancestors.size(); i++) {
            File s = ancestors.get(i);
            FileAuthority fileAuthority = find(sid, s.getId(), principal).orElse(null);
            if(fileAuthority != null){
                authorities = fileAuthority.getAuthorities();
                break;
            }
        }
        return authorities;
    }

    private Optional<FileAuthority> find(Long sid, Long pid, Boolean principal){
        FileAuthority prod = new FileAuthority();
        prod.setSid(sid);
        prod.setPid(pid);
        prod.setPrincipal(principal);
        return fileAuthorityRepository.findOne(Example.of(prod));
    }
}
