package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.AcgnPageParams;
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
//@Anonymous
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
    public RestResponse<PageResult> getAcgnPageByTimeAndCityName(@RequestBody AcgnPageParams acgnPageParams){
        log.info("分页获取漫展演出表：{},cityName：{}", acgnPageParams);
        int page = Math.toIntExact(acgnPageParams.getPage());
        int size = Math.toIntExact(acgnPageParams.getPageSize());
        PageResult<Acgn> acgnPageByTimeAndCityName = acgnService.getAcgnPageByTimeAndCityName(page, size, acgnPageParams.getStartTime(), acgnPageParams.getCityName());

        return RestResponse.success(acgnPageByTimeAndCityName, "查询成功");
    }

    @ApiOperation("上传漫展")
    @PostMapping("/public")
    public RestResponse<String> publicAcgn(@RequestBody Acgn acgn){
        acgnService.insert(acgn);
        return RestResponse.success();
    }
}
