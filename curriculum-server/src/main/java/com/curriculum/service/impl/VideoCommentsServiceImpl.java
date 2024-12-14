package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.mapper.VideoCommentsMapper;
import com.curriculum.model.po.VideoComments;
import com.curriculum.service.VideoCommentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 视频评论 服务实现类
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@Service
public class VideoCommentsServiceImpl extends ServiceImpl<VideoCommentsMapper, VideoComments> implements VideoCommentsService {

}
