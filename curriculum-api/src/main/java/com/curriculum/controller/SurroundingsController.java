package com.curriculum.controller;

import com.curriculum.service.SurroundingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 周边表 前端控制器
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@RestController
@RequestMapping("surroundings")
public class SurroundingsController {

    @Autowired
    private SurroundingsService  surroundingsService;
}
