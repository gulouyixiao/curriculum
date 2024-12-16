package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.PageParams;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.model.vo.VideoToMain;
import com.curriculum.service.VideoBaseService;
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

    @Anonymous
    @PostMapping("page")
    public RestResponse<PageResult> getVideoBasePage(
            PageParams pageParams,
            @RequestParam(required = false) String tags) {
            int page = Math.toIntExact(pageParams.getPageNo());
            int size = Math.toIntExact(pageParams.getPageSize());
            PageResult<VideoToMain> videoBasePage = videoBaseService.getVideoBasePage(page, size, tags);
            return RestResponse.success(videoBasePage, "查询成功");
    }
}
