package com.curriculum.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile file);

    String uploadImage(MultipartFile file);
}
