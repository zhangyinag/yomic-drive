package com.yomic.drive.service.impl;

import ch.qos.logback.core.util.FileUtil;
import com.yomic.drive.config.AppProperties;
import com.yomic.drive.domain.File;
import com.yomic.drive.helper.ContextHelper;
import com.yomic.drive.repository.FileRepository;
import com.yomic.drive.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private FileRepository fileRepository;

    @Override
    public Long saveFile(MultipartFile file, Long id, Long parentId) {
        File f = toFile(file, id, parentId);
        toStorage(f, file);
        f = fileRepository.saveAndFlush(f);
        return f.getId();
    }

    @Override
    public File downloadFile(Long id) {
        File file = fileRepository.findById(id).orElse(null);
        if (file == null || file.getUuid() == null) throw new RuntimeException("未找到文件 [ ID : " + id + "]");
        java.io.File storeFile = fromStorage(file.getUuid());
        if(storeFile == null) throw new RuntimeException("文件已丢失");
        file.setRawFile(storeFile);
        return file;
    }

    private File toFile(MultipartFile file, Long id, Long parentId) {
        assert file != null;

        File f = null;
        if(id != null) {
            f = fileRepository.findById(id).orElse(null);
        }
        if(f == null) {
            f = new File();
        }
        if(parentId != null) {
            File parentFile = fileRepository.findById(parentId).orElse(null);
            if(parentFile != null) f.setParent(parentFile);
        }

        f.setName(file.getOriginalFilename());
        f.setSize(file.getSize());

        String handler = ContextHelper.getCurrentUsername();
        if (f.getId() == null) { // 更新
            f.setIsDir(false);
            f.setUploadBy(handler);
            f.setUploadDate(new Date());
            f.setStatus(true);
        }

        f.setModifyBy(handler);
        f.setModifyDate(new Date());
        f.setUuid(UUID.randomUUID().toString());

        return f;
    }

    private void toStorage (File f, MultipartFile file) {
        assert f != null;
        if (f.getUuid() == null) return;
        java.io.File storeFile = getStoreFile(f.getUuid());

        try {
            FileUtil.createMissingParentDirectories(storeFile);
            file.transferTo(storeFile);
            f.setRawFile(storeFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private java.io.File fromStorage(String uuid) {
        java.io.File storeFile = getStoreFile(uuid);
        if(!storeFile.exists()) return null;
        return storeFile;
    }

    private java.io.File getStoreFile(String uuid) {
        String path = appProperties.getPath();
        return new java.io.File(path, uuid);
    }
}
