package com.curriculum.controller;

import com.curriculum.model.po.Acgn;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.AcgnService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api/curriculum/acgn")
@Api(tags = "漫展演出表")
public class AcgnController {

    @Autowired
    private AcgnService  acgnService;

    @GetMapping("/info/{id}")
    public RestResponse<Acgn> getAcgnInfo(@PathVariable Long id) {
        return RestResponse.success(acgnService.getById(id), "查询成功");
    }
}
