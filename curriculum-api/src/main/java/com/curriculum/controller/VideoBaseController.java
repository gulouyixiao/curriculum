package com.curriculum.controller;

import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.CommentsPageParams;
import com.curriculum.model.dto.MovieDto;
import com.curriculum.model.dto.PageParams;
import com.curriculum.model.dto.VideoPageParams;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.po.VideoComments;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.model.vo.VideoVo;
import com.curriculum.service.FileService;
import com.curriculum.service.MediaFilesService;
import com.curriculum.service.VideoBaseService;
import com.curriculum.service.VideoCommentsService;
import com.curriculum.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

/**
 * 视频，番剧相关接口
 */
@Slf4j
@RestController
@RequestMapping("/api/curriculum")
@Api(tags = "视频，番剧相关接口")
public class VideoBaseController {

    @Autowired
    private VideoBaseService  videoBaseService;

    @Autowired
    private VideoCommentsService videoCommentsService;

    @Autowired
    private FileService fileService;

    @Autowired
    private MediaFilesService mediaFilesService;

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


    /**
     * 获取视频或番剧的评论区
     * @param commentsPageParams
     * @return
     */
    @GetMapping("/comments/page")
    @ApiOperation(value = "获取视频或番剧的评论区")
    @Anonymous
    public RestResponse comments(CommentsPageParams commentsPageParams) {
        log.info("获取视频或番剧的评论区：{}", commentsPageParams);
        PageResult pageResult = videoCommentsService.commentsPageQuery(commentsPageParams);
        return RestResponse.success(pageResult);
    }

    /**
     * 发表评论
     * @param
     * @return
     */
    @PostMapping("/comments/publish")
    @ApiOperation(value = "发表评论")
    public RestResponse commentsPublish(@RequestBody VideoComments videoComments) {
        log.info("发表评论：{}", videoComments);
        videoCommentsService.commentsPublish(videoComments);
        return RestResponse.success();
    }

    /**
     * 上传视频
     * @param file
     * @return
     */
    @Anonymous
    @PostMapping("/upload/{type}")
    @ApiOperation(value = "上传视频")
    public RestResponse upload(@PathVariable String type,
                               @RequestParam(value = "file") MultipartFile file,
                               MovieDto movieDto) {
        log.info("上传视频：{}", file);
        String fileurl = fileService.uploadVideo(file);
        String id = fileurl.substring(fileurl.lastIndexOf("/") + 1);
        long fileSize = file.getSize();
        String movietime = FileUtils.getVideoTime(file);

        double fileSizeKB = fileSize / 1024.0;
        if (type.equals("001002")) {
            mediaFilesService.addMovie(fileurl, file.getName(), (long) fileSizeKB);
            id = id.substring(0, id.indexOf("."));
        }
        else if (type.equals("001003")) {
            mediaFilesService.addAnime(fileurl, file.getName(), (long) fileSizeKB, movieDto, movietime);
            id = null;
        }
        VideoVo videoVo = new VideoVo( id,fileurl);
        return RestResponse.success(videoVo,"上传成功");
    }

    @Anonymous
    @GetMapping("anime/show")
    @ApiOperation(value = "番剧热播前五")
    public RestResponse show() {
        log.info("番剧热播前五");
        PageResult videoBases = videoBaseService.show();
        return RestResponse.success(videoBases,"查询成功");
    }
}
