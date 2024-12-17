package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.PageParams;
import com.curriculum.model.dto.VideoPageParams;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.model.vo.VideoToMain;
import com.curriculum.service.VideoBaseService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/api/curriculum")
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
//    @Anonymous
//    @PostMapping("page1")
//    public RestResponse<PageResult> getVideoBasePage(
//            PageParams pageParams,
//            @RequestParam(required = false) String tags) {
//            int page = Math.toIntExact(pageParams.getPage());
//            int size = Math.toIntExact(pageParams.getPageSize());
//            PageResult<VideoToMain> videoBasePage = videoBaseService.getVideoBasePage(page, size, tags);
//            return RestResponse.success(videoBasePage, "查询成功");
//    }
}
