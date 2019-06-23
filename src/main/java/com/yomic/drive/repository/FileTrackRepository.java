package com.yomic.drive.repository;

import com.yomic.drive.domain.FileTrack;
import com.yomic.drive.repository.common.BaseRepository;

import java.util.List;


public interface FileTrackRepository extends BaseRepository<FileTrack> {
   List<FileTrack> findFileTracksByFileId(Long fileId);
}
