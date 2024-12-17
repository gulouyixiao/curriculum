package com.curriculum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curriculum.model.po.MediaFiles;

import java.util.List;

/**
 * <p>
 * 媒资信息 Mapper 接口
 * </p>
 *
 * @author gulouyixiao
 */
public interface MediaFilesMapper extends BaseMapper<MediaFiles> {

    List<String> getTags();
}
