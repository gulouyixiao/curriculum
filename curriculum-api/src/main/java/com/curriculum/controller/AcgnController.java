package com.curriculum.controller;

import com.curriculum.service.AcgnService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 漫展演出表 前端控制器
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@RestController
@RequestMapping("/api/acgn")
@Api(tags = "漫展演出表")
public class AcgnController {

    @Autowired
    private AcgnService  acgnService;
}
