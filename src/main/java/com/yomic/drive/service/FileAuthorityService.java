package com.yomic.drive.service;


import com.yomic.drive.domain.FileAuthority;

import java.util.List;

public interface FileAuthorityService {

    FileAuthority getFileAuthority(Long sid, Long pid, Boolean principal);

    List<FileAuthority> getFileAuthoritiesBySid(Long sid, Boolean principal);

    List<FileAuthority> getFileAuthoritiesByPid(Long pid);

    Long addFileAuthority(FileAuthority authority);

    Long updateFileAuthority(Long id, Long authorities, Long inherit);

    void deleteFileAuthority(Long id);

    Long getAuthorities(Long sid, Long pid, Boolean principal);

    Long updateFileAuthority(Long id, Long authorities);
}
