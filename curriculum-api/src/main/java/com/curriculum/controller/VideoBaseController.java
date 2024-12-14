package com.curriculum.controller;

import com.curriculum.service.VideoBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 课程基本信息 前端控制器
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@RestController
@RequestMapping("videoBase")
public class VideoBaseController {

    @Autowired
    private VideoBaseService  videoBaseService;
}
