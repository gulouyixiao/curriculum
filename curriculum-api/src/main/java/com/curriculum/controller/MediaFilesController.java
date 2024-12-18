package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.FileService;
import com.curriculum.service.MediaFilesService;
import com.curriculum.utils.FileHashUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 媒资信息 前端控制器
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@RestController
@RequestMapping("/api/curriculum")
@Anonymous
@Api(tags = "媒资信息")
public class MediaFilesController {

    @Autowired
    private MediaFilesService  mediaFilesService;

    @Autowired
    private FileService fileService;

    @GetMapping("/tags")
    @ApiOperation(value = "获取标签")
    public RestResponse<List<String>> getTags() {
        List<String> tags = mediaFilesService.getTags();
        return RestResponse.success(tags, "查询成功");
    }

    // 上传文件测试用
    @PostMapping("/upload")
    public RestResponse<String> upload(@RequestParam(value = "file") MultipartFile file) {
        fileService.upload(file);

        return RestResponse.success("上传成功");
    }

    @Anonymous
    @PostMapping("uploadImage")
    @ApiOperation(value = "上传图片")
    public RestResponse uploadImage(@RequestParam(value = "file") MultipartFile file) {
        log.info("上传图片：{}", file);
        String md5 = FileHashUtils.calculateFileHash(file);
        if (mediaFilesService.selectById(md5)) {
            return RestResponse.validfail("图片已存在");
        }
        String fileurl = fileService.uploadImage(file);
        long fileSize = file.getSize();

        double fileSizeKB = fileSize / 1024.0;
        mediaFilesService.addImage(fileurl,file.getName(), (long) fileSizeKB);
        return RestResponse.success(fileurl,"上传成功");
    }

//    @Anonymous
//    @PostMapping("/upload/{type}")
//    public RestResponse<String> upload(@PathVariable String type, @RequestParam(value = "file") MultipartFile file) {
//        log.info("上传文件：{}", file);
//        String fileurl = fileService.upload(type,file);
//        return RestResponse.success(fileurl,"上传成功");
//    }

}
