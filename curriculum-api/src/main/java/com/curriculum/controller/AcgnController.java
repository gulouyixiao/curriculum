package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.PageParams;
import com.curriculum.model.po.Acgn;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.AcgnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@Anonymous
public class AcgnController {

    @Autowired
    private AcgnService acgnService;

    @ApiOperation(value = "获取漫展演出表")
    @GetMapping("/info/{id}")
    public RestResponse<Acgn> getAcgnInfo(@PathVariable Long id) {
        return RestResponse.success(acgnService.getById(id), "查询成功");
    }

    @ApiOperation(value = "根据时间和城市名获取漫展演出表")
    @PostMapping("/page")
    public RestResponse<PageResult> getAcgnPageByTimeAndCityName(@RequestBody PageParams pageParams, @RequestParam(required = false) String startTime, @RequestParam(required = false) String cityName){
        log.info("分页获取漫展演出表：{}",pageParams);
        int page = Math.toIntExact(pageParams.getPage());
        int size = Math.toIntExact(pageParams.getPageSize());
        PageResult<Acgn> acgnPageByTimeAndCityName = acgnService.getAcgnPageByTimeAndCityName(page, size, startTime, cityName);

        return RestResponse.success(acgnPageByTimeAndCityName, "查询成功");
    }
}
