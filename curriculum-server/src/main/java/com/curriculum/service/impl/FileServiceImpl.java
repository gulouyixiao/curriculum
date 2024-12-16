package com.curriculum.service.impl;

import com.curriculum.service.FileService;
import com.curriculum.utils.MinioUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
public class FileServiceImpl implements FileService {
    @Autowired
    private MinioUtils minioUtil;
    @Override
    public String upload(MultipartFile file) {
        minioUtil.upload("data",file);
        return file.getOriginalFilename();
    }
}
