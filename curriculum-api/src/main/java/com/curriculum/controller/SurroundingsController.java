package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.SurroundingsDTO;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.SurroundingsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 周边表 前端控制器
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@RestController
@RequestMapping("/api/curriculum/surroundings")
public class SurroundingsController {

    @Autowired
    private SurroundingsService  surroundingsService;

    @Anonymous
    @GetMapping("/info/{id}")
    @ApiOperation(value = "周边信息查询")
    public RestResponse info(@PathVariable Integer id) {
        log.info("周边信息查询：{}", id);
        return RestResponse.success(surroundingsService.getById(id));
    }

    /**
     * 周边分页查询
     * @param surroundingsDTO
     * @return
     */

    @Anonymous
    @PostMapping("/page")
    @ApiOperation(value = "周边分页查询")
    public RestResponse page(@RequestBody SurroundingsDTO surroundingsDTO) {
        log.info("周边分页查询：{}", surroundingsDTO);
        PageResult pageResult = surroundingsService.pageQuery(surroundingsDTO);
        return RestResponse.success(pageResult, "查询成功");
    }
}
