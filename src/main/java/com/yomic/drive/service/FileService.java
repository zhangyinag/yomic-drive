package com.yomic.drive.service;

import com.yomic.drive.domain.File;
import org.springframework.web.multipart.MultipartFile;


public interface FileService {

    Long saveFile (MultipartFile file, Long id, Long parentId);

    File downloadFile(Long id);
}
