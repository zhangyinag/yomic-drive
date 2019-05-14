package com.yomic.drive.service;

import com.yomic.drive.domain.Example;

import java.util.List;

public interface ExampleService {
    List<Example> getExampleList();

    Example addExample (Example example);
}
