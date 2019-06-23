package com.yomic.drive.service;

import com.yomic.drive.domain.File;
import com.yomic.drive.domain.RecycleFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileService {

    Long saveFile (MultipartFile file, Long parentId);

    Long updateFile (MultipartFile file, Long id, String desc);

    File downloadFile(Long id);

    List<File> getFiles(Long parentId, Boolean isDir);

    File getFile(Long id);

    Long saveDir(String name, Long parentId);

    Long rename(Long id, String name);

    boolean access(Long userId, Long fileId, Long... bits);

    List<File> getFilesForAuthority(Long parentId, Boolean isDir);

    File createUserDir (String username);

    void deleteFile (Long id);

    void recycleFile (Long id);

    List<RecycleFile> getRecycleFiles();
}
