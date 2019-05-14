package com.yomic.drive.service.impl;

import com.yomic.drive.domain.Example;
import com.yomic.drive.repository.ExampleRepository;
import com.yomic.drive.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleServiceImpl implements ExampleService {

    @Autowired
    private ExampleRepository exampleRepository;

    @Override
    public List<Example> getExampleList() {
        return exampleRepository.findAll();
    }

    @Override
    public Example addExample(Example example) {
        return exampleRepository.save(example);
    }
}
