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

import java.util.ArrayList;
import java.util.List;


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


//    @Override
    public FileAuthority getFileAuthority(Long sid, Long pid, Boolean principal) {
        return null;
    }

    private FileAuthority getAuthorityFromUser(Long sid, Long pid) {
        User user = userRepository.findById(sid).orElse(null);
        if(user == null) return new FileAuthority();
        Long authorities = 0l;
        Long pit = -1l;
        Long sit = -1l;
        FileAuthority fileAuthority = getAuthorityFromFile(user.getId(), pid, false);
        authorities = fileAuthority.getAuthorities() | authorities & pit;
        pit = fileAuthority.getPit() & pit;
        sid = fileAuthority.getSid() & sit;
//        fileAuthority = getAuthorityFromDept()
        return null;
    }

    private FileAuthority getAuthorityFromDept(Long sid, Long pid, Long sit) {
        Dept current = deptRepository.findById(sid).orElse(null);
        if(current == null) return new FileAuthority();
        List<Dept> ancestors = new ArrayList<>();
        while(current != null) {
            ancestors.add(current);
            current = current.getParent();
        }

        Long authorities = 0l;
        Long pit = -1l;
        sid = sit == null ? -1l : sit;
        for (int i = 0; i < ancestors.size(); i++) {
            Dept s = ancestors.get(i);
            FileAuthority fileAuthority = getAuthorityFromFile(s.getId(), pid, false);
            authorities = fileAuthority.getAuthorities() | authorities & pit;
            pit = fileAuthority.getPit() & pit;
            sit = fileAuthority.getSid() & sit;
            if(sit == 0l) break;
        }
        FileAuthority ret = new FileAuthority();
        ret.setAuthorities(authorities);
        return ret;
    }

    private FileAuthority getAuthorityFromFile(Long sid, Long pid, Boolean principal) {
        File current = fileRepository.findById(pid).orElse(null);
        if(current == null) return new FileAuthority();
        List<File> ancestors = new ArrayList<>();
        while(current != null) {
            ancestors.add(current);
            current = current.getParent();
        }

        Long authorities = 0l;
        Long pInherit = -1l;
        Long sInherit = -1l;
        for (int i = 0; i < ancestors.size(); i++) {
            File s = ancestors.get(i);
            FileAuthority prod = new FileAuthority();
            prod.setSid(sid);
            prod.setPid(s.getId());
            prod.setPrincipal(principal);
            FileAuthority fileAuthority = fileAuthorityRepository.findOne(Example.of(prod)).orElse(null);
            if(fileAuthority != null){
                authorities = fileAuthority.getAuthorities() | authorities & pInherit;
                pInherit = fileAuthority.getPit() & pInherit;
                sInherit = fileAuthority.getSit() & pInherit;
                if(pInherit == 0l) break;
            }
        }
        FileAuthority ret = new FileAuthority();
        ret.setPit(pInherit);
        ret.setSit(sInherit);
        ret.setAuthorities(authorities);
        return ret;
    }
}
