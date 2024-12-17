package com.curriculum.service;

import com.alipay.api.domain.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.curriculum.model.dto.CommentsPageParams;
import com.curriculum.model.dto.VideoPageParams;
import com.curriculum.model.po.VideoBase;
import com.curriculum.model.po.VideoComments;
import com.curriculum.model.vo.PageResult;
import com.curriculum.model.vo.VideoBaseVO;

import java.util.List;

/**
 * 视频
 */
public interface VideoBaseService extends IService<VideoBase> {

	/**
	 * 视频条件分页查询
	 * @param videoPageParams
	 * @return
	 */
	PageResult<VideoBase> pageQuery(VideoPageParams videoPageParams);


}
