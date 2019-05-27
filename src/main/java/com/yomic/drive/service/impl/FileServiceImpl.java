package com.yomic.drive.service.impl;

import ch.qos.logback.core.util.FileUtil;
import com.yomic.drive.config.AppProperties;
import com.yomic.drive.domain.File;
import com.yomic.drive.domain.FileAuthority;
import com.yomic.drive.domain.User;
import com.yomic.drive.helper.ContextHelper;
import com.yomic.drive.helper.ExceptionHelper;
import com.yomic.drive.repository.FileRepository;
import com.yomic.drive.repository.UserRepository;
import com.yomic.drive.service.FileAuthorityService;
import com.yomic.drive.service.FileService;
import com.yomic.drive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FileAuthorityService fileAuthorityService;

    @Override
    public Long saveFile(MultipartFile file, Long parentId) {
        assert file != null;
        assert parentId != null;
        access(ContextHelper.getCurrentUserId(), parentId, FileAuthority.NEW);
        File f = toFile(file, parentId);
        toStorage(f, file);
        f = fileRepository.saveAndFlush(f);
        return f.getId();
    }

    @Override
    public File downloadFile(Long id) {
        File file = fileRepository.findById(id).orElseThrow(ExceptionHelper.optionalThrow("not found file: " + id));
        if(file.getIsDir()) throw new RuntimeException("couldn't download dir");
        if(file.getUuid() == null) throw new RuntimeException("file: " + id + "haven't real file");
        java.io.File storeFile = fromStorage(file.getUuid());
        if(storeFile == null) throw new RuntimeException("文件已丢失");
        file.setRawFile(storeFile);
        return file;
    }

    @Override
    public List<File> getFiles(Long parentId) {
        List<File> fileList = Collections.emptyList();
        if(parentId == null){// 根目录
            fileList = new ArrayList<>();
            fileList.add(getRootFile());
        }else{
            File query = new File();
            query.setParentId(parentId);
            query.setStatus(true);
            fileList = fileRepository.findAll(Example.of(query));
        }
        for(int i = 0; i<fileList.size(); i++) {
            preHandle(fileList.get(i));
        }
        return fileList.stream()
                .filter(s -> access(ContextHelper.getCurrentUserId(), s.getId(), FileAuthority.VISIBLE))
                .collect(Collectors.toList());
    }

    @Override
    public File getFile(Long id) {
        File file = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到文件 : [" + id  + "]"));
        this.preHandle(file);
        return file;
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

    @Override
    public File getRootFile() {
        return fileRepository.findFileByParentIsNullAndStatusIsTrue();
    }

    @Override
    public boolean access(Long userId, Long fileId, Long... bits) {
        assert userId != null;
        assert fileId != null;
        User user = null;
        if(userId.equals(ContextHelper.getCurrentUserId())) user = ContextHelper.getCurrentUser();
        else user = userRepository.findById(userId).orElseThrow(ExceptionHelper.optionalThrow("not found user: " + userId));
        if(user.isSuper()) return true;
        if(user.isAdmin()) {
            // TODO 文件管理员是否有该文件的管理权限
            return true;
        }
        Long authorities = fileAuthorityService.getAuthorities(userId, fileId, true);
        if(FileAuthority.hasAuthorities(authorities, bits)) return true;
        return false;
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

    /**
     * 返回前预处理
     * @param file
     */
    private void preHandle(File file) {
        assert file != null;
        assert file.getId() != null;
        Long userId = ContextHelper.getCurrentUser().getId();
        Long authorities = fileAuthorityService.getAuthorities(userId, file.getId(), true);
        file.setAuthorities(authorities);
        file.setChildren(null);
    }

}
