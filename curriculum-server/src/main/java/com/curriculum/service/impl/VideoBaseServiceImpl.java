package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.mapper.VideoBaseMapper;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.VideoToMain;
import com.curriculum.service.VideoBaseService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@Service
public class VideoBaseServiceImpl extends ServiceImpl<VideoBaseMapper, VideoBase> implements VideoBaseService {

    @Autowired
    private VideoBaseMapper videoBaseMapper;
    @Override
    public PageResult<VideoToMain> getVideoBasePage(int page, int size, String tags) {
        PageHelper.startPage(page, size);
        System.out.println("tags:" + tags);
        List<VideoBase> videoBaseList = videoBaseMapper.getAllVideoByTags(tags);
        videoBaseList.forEach(videoBase -> {
            log.info("videoBase:{}", videoBase);
        });

        List<VideoToMain> collect = videoBaseList.stream()
                .map(videoBase -> {
                    VideoToMain videoToMain = new VideoToMain();
                    BeanUtils.copyProperties(videoBase, videoToMain);
                    return videoToMain;
                })
                .collect(Collectors.toList());
        PageResult<VideoToMain> videoToMainPageResult = new PageResult<>(collect, (long) collect.size(), page, size);
        return videoToMainPageResult;
    }
}
