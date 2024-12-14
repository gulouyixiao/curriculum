package com.curriculum.controller;

import com.curriculum.service.MediaFilesService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 媒资信息 前端控制器
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@RestController
@RequestMapping("/api/mediaFiles")
@Api(tags = "媒资信息")
public class MediaFilesController {

    @Autowired
    private MediaFilesService  mediaFilesService;
}
