package com.yomic.drive.service.impl;

import ch.qos.logback.core.util.FileUtil;
import com.yomic.drive.config.AppProperties;
import com.yomic.drive.domain.File;
import com.yomic.drive.helper.ContextHelper;
import com.yomic.drive.repository.FileRepository;
import com.yomic.drive.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private FileRepository fileRepository;

    @Override
    public Long saveFile(MultipartFile file, Long parentId) {
        File f = toFile(file, parentId);
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

    @Override
    public List<File> getFiles(Long parentId) {
        File query = new File();
        query.setParentId(parentId);
        return fileRepository.findAll(Example.of(query));
    }

    @Override
    public File getFile(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到文件 : [" + id  + "]"));
    }

    @Override
    public Long saveDir(String name, Long parentId) {
        assert name != null;
        if(parentId != null) {
            fileRepository.findById(parentId).orElseThrow(() -> new RuntimeException("未找到父级文件夹 [" + parentId + "]"));
        }
        File query = new File();
        query.setParentId(parentId);
        query.setName(name);
        boolean exists = fileRepository.exists(Example.of(query));
        if(exists) throw new RuntimeException("文件夹名称 [" + name + "] 已存在");
        File entity = new File();
        entity.setParentId(parentId);
        entity.setName(name);
        entity.setStatus(true);
        entity.setIsDir(true);
        entity.setUploadDate(new Date());
        entity.setModifyDate(new Date());
        entity.setUploadBy(ContextHelper.getCurrentUsername());
        entity.setModifyBy(ContextHelper.getCurrentUsername());
        entity = fileRepository.saveAndFlush(entity);
        return entity.getId();
    }

    @Override
    public Long rename(Long id, String name) {
        assert id != null;
        assert name != null;
        File file = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到文件 [" + id + "]"));
        file.setName(name);
        fileRepository.saveAndFlush(file);
        return file.getId();
    }

    private File toFile(MultipartFile file, Long parentId) {
        assert file != null;

        if (parentId != null) {
            fileRepository.findById(parentId).orElseThrow(() -> new RuntimeException("未找到父级文件 [" + parentId + "]"));
        }
        // 根据文件名判断新增还是更新
        String filename = file.getOriginalFilename();
        File queryExists = new File();
        queryExists.setName(filename);
        queryExists.setParentId(parentId);
        List<File> rets = fileRepository.findAll(Example.of(queryExists));
        File f = CollectionUtils.isEmpty(rets) ? new File() : rets.get(0);

        if(parentId != null) {
            File parentFile = fileRepository.findById(parentId).orElse(null);
            if(parentFile != null) f.setParent(parentFile);
        }

        f.setName(filename);
        f.setSize(file.getSize());

        String handler = ContextHelper.getCurrentUsername();
        if (f.getId() == null) { // 更新
            f.setIsDir(false);
            f.setUploadBy(handler);
            f.setUploadDate(new Date());
            f.setStatus(true);
            f.setContentType(file.getContentType());
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
