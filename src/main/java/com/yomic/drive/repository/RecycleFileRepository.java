package com.yomic.drive.repository;

import com.yomic.drive.domain.RecycleFile;
import com.yomic.drive.repository.common.BaseRepository;

import java.util.List;


public interface RecycleFileRepository extends BaseRepository<RecycleFile> {
    List<RecycleFile> findRecycleFilesByRecycleBy(String recycleBy);
}
