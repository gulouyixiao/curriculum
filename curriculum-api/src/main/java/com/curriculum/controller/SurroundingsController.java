package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.SurroundingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api/curriculum/surroundings/")
public class SurroundingsController {

    @Autowired
    private SurroundingsService  surroundingsService;

    @Anonymous
    @GetMapping("info/{id}")
    public RestResponse info(@PathVariable Integer id) {
        log.info("周边信息查询：{}", id);
        return RestResponse.success(surroundingsService.getById(id));
    }
}
