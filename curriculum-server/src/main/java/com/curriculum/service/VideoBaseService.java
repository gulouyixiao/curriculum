package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.VideoPageParams;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.VideoToMain;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author gulouyixiao
 * @since 2024-12-14
 */
public interface VideoBaseService extends IService<VideoBase> {

	/**
	 * 视频条件分页查询
	 * @param videoPageParams
	 * @return
	 */
	PageResult pageQuery(VideoPageParams videoPageParams);
    PageResult<VideoToMain> getVideoBasePage(int page, int size, String tags);
}
