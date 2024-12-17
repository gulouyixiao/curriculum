package com.curriculum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.VideoPageParams;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.vo.PageResult;


/**
 * 视频
 */
public interface VideoBaseService extends IService<VideoBase> {

	/**
	 * 视频条件分页查询
	 * @param videoPageParams
	 * @return
	 */
	PageResult pageQuery(VideoPageParams videoPageParams);
}
