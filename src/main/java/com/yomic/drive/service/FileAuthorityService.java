package com.yomic.drive.service;


import com.yomic.drive.domain.FileAuthority;

public interface FileAuthorityService {

    FileAuthority getFileAuthority(Long sid, Long pid, Boolean principal);
}
