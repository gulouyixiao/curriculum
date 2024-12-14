package com.curriculum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.curriculum.mapper.VideoAuditMapper;
import com.curriculum.model.po.VideoAudit;
import com.curriculum.service.VideoAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 视频审核 服务实现类
 * </p>
 *
 * @author gulouyixiao
 */
@Slf4j
@Service
public class VideoAuditServiceImpl extends ServiceImpl<VideoAuditMapper, VideoAudit> implements VideoAuditService {

}
