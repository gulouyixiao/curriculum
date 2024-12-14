package com.curriculum.controller;

import com.curriculum.service.VideoAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 视频审核 前端控制器
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@RestController
@RequestMapping("videoAudit")
public class VideoAuditController {

    @Autowired
    private VideoAuditService  videoAuditService;
}
