package com.yomic.drive.repository;

import com.yomic.drive.domain.File;
import com.yomic.drive.repository.common.BaseRepository;

import java.util.List;

public interface FileRepository extends BaseRepository<File> {
    // 根级共享文件
    List<File> findFilesByParentIsNullAndStatusIsTrueAndShareIsTrueAndIsDir(Boolean isDir);

    List<File> findFilesByParentAndIsDirAndStatusIsTrue (File parent, Boolean isDir);

    List<File> findFilesByParentAndStatusIsTrue (File parent);

    // 用户根级私有目录
    File findFileByShareIsFalseAndParentIsNullAndUploadBy(String uploadBy);
}
