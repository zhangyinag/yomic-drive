package com.yomic.drive.service.impl;

import com.yomic.drive.domain.File;
import com.yomic.drive.domain.Link;
import com.yomic.drive.helper.AssertHelper;
import com.yomic.drive.helper.ContextHelper;
import com.yomic.drive.repository.FileRepository;
import com.yomic.drive.repository.LinkRepository;
import com.yomic.drive.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private FileRepository fileRepository;


    @Override
    public Long saveLink(Link link) {
        AssertHelper.assertNotNull(link, link.getFileList());
        List<File> fileList = link.getFileList();
        List<File> pFileList = new ArrayList<>();
        for(File f : fileList) {
            File item = fileRepository.findById(f.getId()).orElse(null);
            if (item != null) pFileList.add(item);
        }
        link.setFileList(pFileList);
        link = linkRepository.saveAndFlush(link);
        return link.getId();
    }

    @Override
    public List<Link> getLinks() {
        String username = ContextHelper.getCurrentUsername();
        return linkRepository.findLinksByGenerateBy(username);
    }

    @Override
    public Link getLink(Long id) {
        return this.linkRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteLink(Long id) {
        linkRepository.deleteById(id);
    }
}
