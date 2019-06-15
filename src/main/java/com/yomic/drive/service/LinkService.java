package com.yomic.drive.service;


import com.yomic.drive.domain.Link;

import java.util.List;


public interface LinkService {
    Long saveLink(Link link);

    List<Link> getLinks();

    Link getLink(Long id);

    void deleteLink(Long id);
}
