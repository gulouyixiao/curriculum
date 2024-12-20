package com.curriculum.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curriculum.annotation.Anonymous;
import com.curriculum.model.dto.*;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.po.VideoComments;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.RestResponse;
import com.curriculum.model.vo.VideoVo;
import com.curriculum.service.FileService;
import com.curriculum.service.MediaFilesService;
import com.curriculum.service.VideoBaseService;
import com.curriculum.service.VideoCommentsService;
import com.curriculum.utils.FileHashUtils;
import com.curriculum.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @ApiOperation(value = "视频番剧分类分页查询")
    @Anonymous

    public RestResponse<PageResult> page(@RequestBody VideoPageParams videoPageParams) {
        log.info("分类番剧分页查询：{}", videoPageParams);
        PageResult pageResult = videoBaseService.PageQuery(videoPageParams);
        return RestResponse.success(pageResult);
    }

    /**
     * 标签查询
     */
    @Anonymous
    @GetMapping("/tags")
    @ApiOperation("获取关键字，标签")
    public RestResponse getTags() {
        List<String> tags = videoBaseService.getTags();
        return RestResponse.success(tags, "查询成功");
    }

    /**
     * 番剧图片轮播推荐
     * @return
     */
    @GetMapping("/anime/recommend")
    @ApiOperation(value = "番剧图片轮播推荐")
    @Anonymous
    public RestResponse<List<VideoBase>> recommend() {
        log.info("番剧图片轮播推荐");
        List<VideoBase> videoBases = videoBaseService.recommend("001003",6);
        return RestResponse.success(videoBases);
    }

    @Anonymous
    @GetMapping("anime/show")
    @ApiOperation(value = "番剧热播前五")
    public RestResponse show() {
        log.info("番剧热播前五");
        List<VideoBase> videoBases = videoBaseService.show("001003",5);
        return RestResponse.success(videoBases,"查询成功");
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
    public RestResponse commentsPublish(@RequestBody CommentsDTO commentsDTO) {
        log.info("发表评论：{}", commentsDTO);
        videoCommentsService.commentsPublish(commentsDTO);
        return RestResponse.success();
    }

    /**
     * 上传视频
     * @param
     * @return
     */
    @PostMapping("/video/publish")
    @ApiOperation(value = "发布视频")
    public RestResponse videoPublish(@RequestBody VideoPublishDto videoPublishDto) {
        log.info("发布视频：{}", videoPublishDto);
        videoBaseService.videoPublish(videoPublishDto);
        return RestResponse.success();
    }

    /**
     * 发布番剧
     * @param
     * @return
     */
    @PostMapping("/anime/publish")
    @ApiOperation(value = "发布番剧")
    public RestResponse animePublish(@RequestBody VideoPublishDto videoPublishDto) {
        log.info("发布番剧：{}", videoPublishDto);
        videoBaseService.animePublish(videoPublishDto);
        return RestResponse.success();
    }

    /**
     * 我的投稿
     * @param
     * @return
     */
    @GetMapping("/submit")
    @ApiOperation(value = "我的投稿")
    public RestResponse<PageResult> submit(PageParams pageParams) {
        log.info("我的投稿：{}", pageParams);
        if(pageParams.getPage() == null){
            pageParams.setPage(1L);
        }
        if(pageParams.getPageSize() == null){
            pageParams.setPageSize(10L);
        }
        PageResult auditList =  videoBaseService.submit(pageParams);
        return RestResponse.success(auditList);
    }

    @Anonymous
    @PostMapping("/upload/{type}")
    @ApiOperation(value = "上传视频番剧")
    public RestResponse upload(@PathVariable String type,
                               @RequestParam(value = "file") MultipartFile file,
                               @RequestParam(required = false) String grade,
                               @RequestParam(required = false) Long parentid,
                                @RequestParam(required = false) String title) {
        log.info("上传视频：{}", file);
        MovieDto movieDto = new MovieDto();
        movieDto.setGrade(grade);
        movieDto.setTitle(title);
        movieDto.setParentId(parentid);
        log.info("上传视频：{}", file);
        String md5 = FileHashUtils.calculateFileHash(file);
        if (mediaFilesService.selectById(md5)) {
            return RestResponse.validfail("图片已存在");
        }
        String fileurl = fileService.uploadVideo(file);
        String id = fileurl.substring(fileurl.lastIndexOf("/") + 1);
        long fileSize = file.getSize();
        String movietime = FileUtils.getVideoTime(file);

        double fileSizeKB = fileSize / 1024.0;
        if (type.equals("001002")) {
            mediaFilesService.addMovie(fileurl, file.getOriginalFilename(), (long) fileSizeKB, movietime);
            id = id.substring(0, id.indexOf("."));
            log.info("视频插入数据库：{}", file);
        }
        else if (type.equals("001003")) {
            mediaFilesService.addAnime(fileurl, file.getOriginalFilename(), (long) fileSizeKB, movieDto, movietime);
            id = null;
        }
        VideoVo videoVo = new VideoVo( id,fileurl);
        return RestResponse.success(videoVo,"上传成功");
    }

    @Anonymous
    @GetMapping("/videovie")
    @ApiOperation(value = "视频播放")
    public RestResponse videovie(@RequestParam int id) {
        log.info("视频播放：{}", id);
        VideoBase videoVo = videoBaseService.videovie(id);
        return RestResponse.success(videoVo,"查询成功");
    }



}
