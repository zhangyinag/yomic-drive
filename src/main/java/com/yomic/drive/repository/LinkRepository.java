package com.yomic.drive.repository;

import com.yomic.drive.domain.Link;
import com.yomic.drive.repository.common.BaseRepository;

import java.util.List;


public interface LinkRepository extends BaseRepository<Link> {
    List<Link> findLinksByGenerateBy(String username);
}
