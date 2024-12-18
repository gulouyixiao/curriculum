package com.curriculum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curriculum.model.po.VideoBase;

import java.util.List;

/**
 * <p>
 * 课程基本信息 Mapper 接口
 * </p>
 *
 * @author gulouyixiao
 */
public interface VideoBaseMapper extends BaseMapper<VideoBase> {

    List<VideoBase> getAllVideoByTags(String tags);

    List<VideoBase> GroupByParentId();

}
