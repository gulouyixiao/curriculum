package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.VideoPageParams;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.service.VideoBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 视频相关接口
 */
@Slf4j
@RestController
@RequestMapping("/api/curriculum")
@Api(tags = "视频相关接口")
public class VideoBaseController {

    @Autowired
    private VideoBaseService  videoBaseService;

    /**
     * 分类分页查询
     * @param videoPageParams
     * @return
     */
    @PostMapping("/page")
    @ApiOperation(value = "视频分类分页查询")
    @Anonymous
    public RestResponse page(VideoPageParams videoPageParams) {
        log.info("视频分类分页查询：{}", videoPageParams);
        PageResult pageResult = videoBaseService.pageQuery(videoPageParams);
        return RestResponse.success(pageResult);
    }
}
