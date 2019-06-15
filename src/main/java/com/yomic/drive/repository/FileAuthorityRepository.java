package com.yomic.drive.repository;

import com.yomic.drive.domain.FileAuthority;
import com.yomic.drive.repository.common.BaseRepository;

import java.util.List;

public interface FileAuthorityRepository extends BaseRepository<FileAuthority> {
    FileAuthority findFileAuthorityBySidAndPidAndPrincipal(Long sid, Long pid, boolean principle);

    List<FileAuthority> findFileAuthoritiesBySidAndPrincipal(Long sid, Boolean principal);

    List<FileAuthority> findFileAuthoritiesByPid(Long pid);
}
