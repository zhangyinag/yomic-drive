package com.yomic.drive.repository;

import com.yomic.drive.domain.File;
import com.yomic.drive.repository.common.BaseRepository;

import java.util.List;

public interface FileRepository extends BaseRepository<File> {
    File findFileByParentIsNullAndStatusIsTrue();

    List<File> findFilesByParentAndIsDirAndStatusIsTrue (File parent, Boolean isDir);

    List<File> findFilesByParentAndStatusIsTrue (File parent);
}
